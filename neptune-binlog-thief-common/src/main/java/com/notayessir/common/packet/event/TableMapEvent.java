package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class TableMapEvent extends Packet {


    private final long tableId;

    private final int flags;

//    private final int schemaNameLength;

    private final String schemaName;

//    private final byte filter1;

//    private final int tableNameLength;

    private final String tableName;

//    private final byte filter2;

    private final int columnCount;

    private final byte [] columnDef;

    private final byte [] columnMetaDef;

    private final byte [] nullMask;

    private final byte [] other;

    public TableMapEvent(PacketHeader packetHeader, EventHeader eventHeader, long tableId, int flags,
                         String schemaName, String tableName,
                         int columnCount, byte[] columnDef, byte[] columnMetaDef, byte[] nullMask, byte [] other) {
        this.eventHeader = eventHeader;
        this.packetHeader = packetHeader;
        this.tableId = tableId;
        this.flags = flags;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnCount = columnCount;
        this.columnDef = columnDef;
        this.columnMetaDef = columnMetaDef;
        this.nullMask = nullMask;
        this.other = other;
    }


    public byte[] getOther() {
        return other;
    }

    public byte[] getNullMask() {
        return nullMask;
    }

    public long getTableId() {
        return tableId;
    }

    public int getFlags() {
        return flags;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public byte[] getColumnDef() {
        return columnDef;
    }

    public byte[] getColumnMetaDef() {
        return columnMetaDef;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.TABLE_MAP_EVENT;
    }
}
