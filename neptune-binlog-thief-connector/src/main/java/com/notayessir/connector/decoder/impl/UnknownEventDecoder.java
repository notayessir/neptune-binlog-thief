package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.UnknownEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.UNKNOWN_EVENT)
public class UnknownEventDecoder extends AbsPacketDecoder<UnknownEvent> {

    @Override
    public PacketWrapper<UnknownEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        EventHeader eventHeader = decodeEventHeader(buf);
        byte [] data = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        UnknownEvent event = new UnknownEvent(packetHeader, eventHeader, data);

        buf.release();
        return new PacketWrapper<>(event);
    }


}
