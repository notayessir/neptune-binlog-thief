package com.notayessir.processor.handler.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.packet.event.TableMap;
import com.notayessir.common.packet.event.TableMapEvent;
import com.notayessir.common.util.MetadataUtil;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.filter.IgnoreFilter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


@Component
public class TableMapEventHandler extends AbsPacketHandler<TableMapEvent> {

    @Autowired
    IgnoreFilter ignoreFilter;


    @Autowired
    TableMapEventCacheManager cacheManager;


    @Override
    public void handle(TableMapEvent packet) {
        if (cacheManager.contain(packet.getTableId())){
            return;
        }
        TableMap tableMap = buildTableMapInfo(packet);
        cacheManager.put(tableMap);
    }

    @Override
    public boolean skipHandle(TableMapEvent packet, AppConfiguration appConfiguration) {
        return ignoreFilter.filter(appConfiguration, packet.getSchemaName(), packet.getTableName());
    }


    private TableMap buildTableMapInfo(TableMapEvent packet) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        BitSet bitSet = BitSet.valueOf(packet.getNullMask());

        byte[] columnMetaDef = packet.getColumnMetaDef();
//        System.out.println("columnMetaDef:" + Arrays.toString(columnMetaDef));
        ByteBuf in = Unpooled.wrappedBuffer(columnMetaDef);

        byte[] columnDefArr = packet.getColumnDef();
        for (int i = 0; i < columnDefArr.length; i++){
            ColumnType columnType = ColumnType.getByVal(Byte.toUnsignedInt(columnDefArr[i]));
            // read metadata for each field
            byte [] metadata = MetadataUtil.readMetadata(columnType, in);
//            System.out.println("columnType: " + columnType + " metadata: " + Arrays.toString(metadata) +" columnDefArr[i]:"+Byte.toUnsignedInt(columnDefArr[i]));
            ColumnDef columnDef = new ColumnDef(i, bitSet.get(i), columnType, metadata);
            columnDefs.add(columnDef);
        }
        in.release();
        return new TableMap(packet.getSchemaName(), packet.getTableName(), packet.getTableId(), columnDefs);
    }
}
