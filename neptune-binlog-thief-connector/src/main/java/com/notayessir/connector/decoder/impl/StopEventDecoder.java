package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.StopEvent;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.STOP_EVENT)
public class StopEventDecoder extends AbsPacketDecoder<StopEvent> {

    @Override
    public PacketWrapper<StopEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        EventHeader eventHeader = decodeEventHeader(buf);
        StopEvent event = new StopEvent(packetHeader, eventHeader);

        buf.release();
        return new PacketWrapper<>(event);
    }

}
