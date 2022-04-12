package com.notayessir.common.packet.event;


import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;

public class RowsEventV2 extends Packet {

    private final Long tableId;

    private final int flags;

    private final int columnNum;

    private final byte [] columnsPresentBitmap;

    private final byte [] columnsPresentBitmapForUpdate;

    private final byte [] rowsData;

    public RowsEventV2(PacketHeader packetHeader, EventHeader eventHeader, Long tableId,
                       int flags, int columnNum,
                       byte[] columnsPresentBitmap, byte [] columnsPresentBitmapForUpdate,
                       byte[] rowsData) {
        this.packetHeader = packetHeader;
        this.eventHeader = eventHeader;
        this.tableId = tableId;
        this.flags = flags;
        this.columnNum = columnNum;
        this.columnsPresentBitmap = columnsPresentBitmap;
        this.rowsData = rowsData;
        this.columnsPresentBitmapForUpdate = columnsPresentBitmapForUpdate;
    }


    public byte[] getColumnsPresentBitmapForUpdate() {
        return columnsPresentBitmapForUpdate;
    }

    public Long getTableId() {
        return tableId;
    }

    public int getFlags() {
        return flags;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public byte[] getColumnsPresentBitmap() {
        return columnsPresentBitmap;
    }

    public byte[] getRowsData() {
        return rowsData;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.getByVal(this.eventHeader.getEventType());
    }
}
