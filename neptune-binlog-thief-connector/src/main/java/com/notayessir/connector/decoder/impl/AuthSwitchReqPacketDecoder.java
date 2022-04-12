package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.AuthSwitchReqPacket;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.AUTH_SWITCH_REQ_PACKET)
public class AuthSwitchReqPacketDecoder extends AbsPacketDecoder<AuthSwitchReqPacket> {


    @Override
    public PacketWrapper<AuthSwitchReqPacket> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        int status = Byte.toUnsignedInt(buf.readByte());
        String authPluginName = ByteUtil.readStringNull(buf);
        byte [] authPluginData = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        AuthSwitchReqPacket packet = new AuthSwitchReqPacket(packetHeader, status, authPluginName, authPluginData);

        buf.release();
        return new PacketWrapper<>(packet);
    }

}
