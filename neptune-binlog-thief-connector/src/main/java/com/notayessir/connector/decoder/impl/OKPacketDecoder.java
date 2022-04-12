package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.OKPacket;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.GENERIC_OK_PACKET)
public class OKPacketDecoder extends AbsPacketDecoder<OKPacket> {


    @Override
    public PacketWrapper<OKPacket> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());


        int header = Byte.toUnsignedInt(buf.readByte());
        int affectedRows = ByteUtil.readEncodedInt(buf);
        int lastInsertId = ByteUtil.readEncodedInt(buf);
        int statusFlag = ByteUtil.readIntAndRelease(buf.readBytes(2));
        int okWarnings = ByteUtil.readIntAndRelease(buf.readBytes(2));
        OKPacket okPacket = new OKPacket(packetHeader, header, affectedRows, lastInsertId, statusFlag, okWarnings);

        buf.release();
        return new PacketWrapper<>(okPacket);
    }

}
