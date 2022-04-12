package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.HandshakeReqPacket;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.common.util.CapabilityUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.HANDSHAKE_REQ_PACKET)
public class HandshakeReqPacketDecoder extends AbsPacketDecoder<HandshakeReqPacket> {


    @Override
    public PacketWrapper<HandshakeReqPacket> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        int protocolVersion = Byte.toUnsignedInt(buf.readByte());
        String serverVersion = ByteUtil.readStringNull(buf);
        int connectionId = ByteUtil.readIntAndRelease(buf.readBytes(4));
        byte [] authPluginDataPart1 = ByteUtil.readBytesAndRelease(buf.readBytes(8));
        // filler 00
        buf.skipBytes(1);
        byte [] lowerCapabilityFlag = ByteUtil.readBytesAndRelease(buf.readBytes(2));
        int characterSet = Byte.toUnsignedInt(buf.readByte());
        int statusFlag = ByteUtil.readIntAndRelease(buf.readBytes(2));
        byte [] upperCapabilityFlag = ByteUtil.readBytesAndRelease(buf.readBytes(2));
        int authPluginDataLength = Byte.toUnsignedInt(buf.readByte());
        // String reserved;
        buf.skipBytes(10);
        // assert ClientSecureConn is set and read authPluginDataPart2
        byte [] authPluginDataPart2 = ByteUtil.readBytesAndRelease(buf.readBytes(Math.max(13, authPluginDataLength - 8)));;
        int capabilityFlag = ByteUtil.parseCapabilityFlag(lowerCapabilityFlag, upperCapabilityFlag);
        String authPluginName = null;
        if (CapabilityUtil.isClientPluginAuth(capabilityFlag)){
            authPluginName = ByteUtil.readStringNull(buf);
        }
        HandshakeReqPacket packet = new HandshakeReqPacket(packetHeader, protocolVersion, serverVersion, connectionId, authPluginDataPart1,
                characterSet, statusFlag, authPluginDataLength, authPluginDataPart2, capabilityFlag, authPluginName);

        buf.release();
        return new PacketWrapper<>(packet);
    }

}
