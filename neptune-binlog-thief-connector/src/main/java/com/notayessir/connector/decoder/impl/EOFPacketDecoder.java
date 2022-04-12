package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EOFPacket;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.GENERIC_EOF_PACKET)
public class EOFPacketDecoder extends AbsPacketDecoder<EOFPacket> {

    @Override
    public PacketWrapper<EOFPacket> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        int header = Byte.toUnsignedInt(buf.readByte());
        int warnings = ByteUtil.readIntAndRelease(buf.readBytes(2));
        int statusFlag = ByteUtil.readIntAndRelease(buf.readBytes(2));
        EOFPacket eofPacket = new EOFPacket(packetHeader, header, warnings, statusFlag);

        buf.release();
        return new PacketWrapper<>(eofPacket);
    }

}
