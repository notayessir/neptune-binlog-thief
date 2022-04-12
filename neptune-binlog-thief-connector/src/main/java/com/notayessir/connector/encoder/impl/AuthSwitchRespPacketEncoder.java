package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.common.packet.AuthSwitchRespPacket;
import com.notayessir.common.packet.PacketType;
import io.netty.buffer.ByteBuf;

@Encoder(PacketType.AUTH_SWITCH_RESP_PACKET)
public class AuthSwitchRespPacketEncoder extends AbsPacketEncoder<AuthSwitchRespPacket> {


    @Override
    public void encodeBody(AuthSwitchRespPacket packet, ByteBuf out) {
        out.writeBytes(packet.getAuthPluginResponse());
    }
}
