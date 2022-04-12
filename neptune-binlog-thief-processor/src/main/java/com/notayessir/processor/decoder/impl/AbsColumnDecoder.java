package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.event.RowsEventV2;
import com.notayessir.common.packet.event.TableMap;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.ColumnDecoder;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.RowSet;
import com.notayessir.processor.decoder.column.Column;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.reflections.Reflections;

import java.util.*;

public abstract class AbsColumnDecoder<C extends Column<?>> implements ColumnDecoder<C> {


    private static final HashMap<ColumnType, ColumnDecoder<?>> columnDecoderMap = new HashMap<>();

    private final static String PACKAGE_NAME = AbsColumnDecoder.class.getPackage().getName();


    static {
        Reflections reflections = new Reflections(PACKAGE_NAME);
        try {
            Set<Class<?>> clz = reflections.getTypesAnnotatedWith(Decoder.class);
            for (Class<?> decoder : clz){
                Decoder annotation = decoder.getAnnotation(Decoder.class);
                columnDecoderMap.put(annotation.value(), (ColumnDecoder<?>) decoder.newInstance());
            }
        }catch (Exception e){
            throw new RuntimeException("error happened when scan column decoder.");
        }
    }


    public static ColumnDecoder<?> getColumnDecoder(ColumnType columnType){
        return columnDecoderMap.get(columnType);
    }


    /**
     * deserialize bytes base on table map info
     * @param tableMap     table map which from [table map] packet
     * @param packet        update/delete/insert packet
     * @return              columns
     */
    public static List<RowSet> readRows(TableMap tableMap, RowsEventV2 packet){
        ByteBuf buf = Unpooled.wrappedBuffer(packet.getRowsData());
        BitSet bitSet = BitSet.valueOf(packet.getColumnsPresentBitmap());

        List<RowSet> list = new ArrayList<>();
        do {
            byte [] nullMask = ByteUtil.readBytesAndRelease(buf.readBytes((bitSet.length() + 7) / 8));
            // parse columns from binary bytes
            List<Column<?>> newColumns = readColumns(tableMap, nullMask, buf);
            List<Column<?>> oldColumns = null;
            PacketType type = PacketType.getByVal(packet.getEventHeader().getEventType());
            if (type == PacketType.UPDATE_ROWS_EVENTv2){
                oldColumns = newColumns;
                bitSet = BitSet.valueOf(packet.getColumnsPresentBitmapForUpdate());
                nullMask = ByteUtil.readBytesAndRelease(buf.readBytes((bitSet.length() + 7) / 8));
                newColumns = readColumns(tableMap, nullMask, buf);
            }
            RowSet rowSet = new RowSet(newColumns, oldColumns);
            list.add(rowSet);
        } while (buf.readableBytes() > 4); // stop while the remains bytes are checksum
        buf.release();
        return list;
    }



    /**
     * deserialize columns from bytes with table map info
     * @param tableMap  table info
     * @param nullMask      mask of column
     * @param in            binary bytes
     * @return              columns
     */
    private static List<Column<?>> readColumns(TableMap tableMap, byte [] nullMask, ByteBuf in){
        List<Column<?>> list = new ArrayList<>();
        BitSet bitSet = BitSet.valueOf(nullMask);
        List<ColumnDef> columnDefs = tableMap.getColumnDefs();
        for (ColumnDef columnDef : columnDefs){
            if (!bitSet.get(columnDef.getPos())){
                ColumnDecoder<?> columnDecoder = AbsColumnDecoder.getColumnDecoder(columnDef.getColumnType());
                Column<?> column = columnDecoder.decode(in, columnDef);
                column.setPos(columnDef.getPos());
                list.add(column);
            }
        }
        return list;
    }


}
