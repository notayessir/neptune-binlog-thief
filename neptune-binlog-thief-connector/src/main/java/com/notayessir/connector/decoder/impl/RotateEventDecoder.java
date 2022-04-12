package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.RotateEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.ROTATE_EVENT)
public class RotateEventDecoder extends AbsPacketDecoder<RotateEvent> {

    @Override
    public PacketWrapper<RotateEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        // find out if there is a checksum end at tail of bytes
        boolean endWithChecksum = isEndWithChecksum(buf);

        EventHeader eventHeader = decodeEventHeader(buf);
        long pos = ByteUtil.readLongAndRelease(buf.readBytes(8));
        String nextBinlogName;
        if (endWithChecksum){
            nextBinlogName = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes() - 4));
        } else {
            nextBinlogName = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes()));
        }
        RotateEvent event = new RotateEvent(packetHeader, eventHeader, pos, nextBinlogName);

        buf.release();
        return new PacketWrapper<>(event);
    }


}
