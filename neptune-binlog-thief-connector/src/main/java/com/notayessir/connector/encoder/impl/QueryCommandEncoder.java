package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.common.packet.ComQueryPacket;
import com.notayessir.common.packet.PacketType;
import io.netty.buffer.ByteBuf;

@Encoder(PacketType.COM_QUERY)
public class QueryCommandEncoder extends AbsPacketEncoder<ComQueryPacket> {


    @Override
    public void encodeBody(ComQueryPacket packet, ByteBuf out) {
        out.writeByte(packet.getCommandId());
        out.writeBytes(packet.getQueryText().getBytes());
    }
}
