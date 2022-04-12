package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.RowQueryEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.ROWS_QUERY_EVENT)
public class RowQueryEventDecoder extends AbsPacketDecoder<RowQueryEvent> {


    @Override
    public PacketWrapper<RowQueryEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());
        // find out if there is a checksum end at tail of bytes
        boolean endWithChecksum = isEndWithChecksum(buf);

        EventHeader eventHeader = decodeEventHeader(buf);
        int len = buf.readByte();
        String query;
        if (endWithChecksum){
            query = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes() - 4));
        }else {
            query = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes()));
        }
        RowQueryEvent event = new RowQueryEvent(packetHeader, eventHeader, len, query);
        buf.release();
        return new PacketWrapper<>(event);
    }



}
