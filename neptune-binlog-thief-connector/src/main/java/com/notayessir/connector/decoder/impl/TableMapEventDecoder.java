package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.TableMapEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.TABLE_MAP_EVENT)
public class TableMapEventDecoder extends AbsPacketDecoder<TableMapEvent> {

    @Override
    public PacketWrapper<TableMapEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        int startPos = buf.readerIndex();
        EventHeader eventHeader = decodeEventHeader(buf);
        long tableId = ByteUtil.readLongAndRelease(buf.readBytes(6));
        int flags = ByteUtil.readIntAndRelease(buf.readBytes(2));
        int schemaNameLength = Byte.toUnsignedInt(buf.readByte());
        String schemaName = ByteUtil.readStringAndRelease(buf.readBytes(schemaNameLength));
        byte filter1 = buf.readByte();
        int tableNameLength = Byte.toUnsignedInt(buf.readByte());
        String tableName = ByteUtil.readStringAndRelease(buf.readBytes(tableNameLength));
        byte filter2 = buf.readByte();
        int columnCount = ByteUtil.readEncodedInt(buf);
        byte [] columnDef = ByteUtil.readBytesAndRelease(buf.readBytes(columnCount));
        int columnMetaDefLen = ByteUtil.readEncodedInt(buf);
        byte [] columnMetaDef = ByteUtil.readBytesAndRelease(buf.readBytes(columnMetaDefLen));
        byte [] nullMask = ByteUtil.readBytesAndRelease(buf.readBytes((columnCount + 7) / 8));
        int endPos = buf.readerIndex();
        byte [] other = ByteUtil.readBytesAndRelease(buf.readBytes(packetHeader.getPayloadLength() - (endPos - startPos)));
        TableMapEvent event = new TableMapEvent(packetHeader, eventHeader, tableId, flags, schemaName,
                tableName, columnCount, columnDef, columnMetaDef, nullMask, other);

        buf.release();
        return new PacketWrapper<>(event);
    }


}
