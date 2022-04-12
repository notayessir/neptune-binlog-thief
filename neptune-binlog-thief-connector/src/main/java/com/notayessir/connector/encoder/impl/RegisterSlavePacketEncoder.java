package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.RegisterSlavePacket;
import io.netty.buffer.ByteBuf;

@Encoder(PacketType.REGISTER_SLAVE_PACKET)
public class RegisterSlavePacketEncoder extends AbsPacketEncoder<RegisterSlavePacket> {

    @Override
    public void encodeBody(RegisterSlavePacket packet, ByteBuf out) {
        out.writeByte(packet.getRegisterSlave());
        out.writeBytes(packet.getServerId());
        out.writeByte(packet.getSlaveHostNameLen());
        out.writeBytes(packet.getSlaveHostName());
        out.writeByte(packet.getSlaveUserLen());
        out.writeBytes(packet.getSlaveUser());
        out.writeByte(packet.getSlavePassLen());
        out.writeBytes(packet.getSlavePass());
        out.writeBytes(packet.getMysqlPort());
        out.writeBytes(packet.getRepRank());
        out.writeBytes(packet.getMasterId());
    }
}
