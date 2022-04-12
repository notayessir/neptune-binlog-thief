package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.common.packet.HandshakeRespPacket;
import com.notayessir.common.packet.PacketType;
import io.netty.buffer.ByteBuf;

@Encoder(PacketType.HANDSHAKE_RESP_PACKET)
public class HandshakeRespPacketEncoder extends AbsPacketEncoder<HandshakeRespPacket> {


    @Override
    public void encodeBody(HandshakeRespPacket packet, ByteBuf out) {
        out.writeBytes(packet.getCapabilityFlags());
        out.writeBytes(packet.getMaxPacketSize());
        out.writeByte((byte) 8);
        out.writeBytes(packet.getReserved());
        out.writeBytes(packet.getUsername());
        out.writeByte(packet.getAuthResponseLen());
        out.writeBytes(packet.getAuthResponse());
    }
}
