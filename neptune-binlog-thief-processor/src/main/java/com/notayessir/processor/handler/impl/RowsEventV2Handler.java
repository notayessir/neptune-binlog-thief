package com.notayessir.processor.handler.impl;

import com.notayessir.common.packet.event.RowsEventV2;
import com.notayessir.common.packet.event.TableMap;
import com.notayessir.processor.configure.AppConfiguration;
import com.notayessir.processor.database.entity.ColumnDefEntity;
import com.notayessir.processor.database.manager.ColumnManager;
import com.notayessir.processor.decoder.RowSet;
import com.notayessir.processor.decoder.impl.AbsColumnDecoder;
import com.notayessir.processor.disruptor.BinlogEvent;
import com.notayessir.processor.filter.IgnoreFilter;
import com.notayessir.processor.filter.MatchFilter;
import com.notayessir.processor.util.JSONFormatData;
import com.notayessir.processor.util.JSONFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;


public class RowsEventV2Handler extends AbsPacketHandler<RowsEventV2> {


    @Autowired
    private ColumnManager columnManager;

    @Autowired
    private IgnoreFilter ignoreFilter;

    @Autowired
    private MatchFilter matchFilter;


    @Autowired
    private TableMapEventCacheManager cacheManager;

    @Autowired
    private RowsEventMessenger rowsEventMessenger;



    @Override
    public void handle(RowsEventV2 packet) {
        TableMap tableMap = cacheManager.get(packet.getTableId());
        doHandle(packet, tableMap);
    }

    @Override
    public boolean skipHandle(RowsEventV2 packet, AppConfiguration appConfiguration) {
        TableMap tableMap = cacheManager.get(packet.getTableId());
        if (Objects.isNull(tableMap)){
            return true;
        }
        if (ignoreFilter.filter(appConfiguration, tableMap.getSchemaName(), tableMap.getTableName())){
            return false;
        }
        return matchFilter.filter(tableMap);
    }


    private void doHandle(RowsEventV2 packet, TableMap tableMap){
        // deserialize the columns
        List<RowSet> rowSets = AbsColumnDecoder.readRows(tableMap, packet);

        // format the columns
        List<ColumnDefEntity> columnDefs = columnManager.find(tableMap.getSchemaName(), tableMap.getTableName());
        List<JSONFormatData> formattedSets = JSONFormatter.format(columnDefs, rowSets);

        BinlogEvent [] events = buildBinlogEvents(packet, tableMap, formattedSets);

        rowsEventMessenger.pushEvent(events);


    }

    private BinlogEvent [] buildBinlogEvents(RowsEventV2 packet, TableMap tableMap, List<JSONFormatData> list){
        BinlogEvent [] events = new BinlogEvent[list.size()];
        for (int i = 0 ; i < list.size(); i++) {
            JSONFormatData formatData = list.get(i);
            events[i] = new BinlogEvent(packet.getPacketType(), tableMap,
                    formatData.getData(), formatData.getOldData());
            events[i].setEventHeader(packet.getEventHeader());
            events[i].setPacketHeader(packet.getPacketHeader());
        }
        return events;
    }





}
