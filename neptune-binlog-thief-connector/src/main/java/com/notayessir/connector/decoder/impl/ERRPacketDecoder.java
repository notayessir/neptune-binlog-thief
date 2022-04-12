package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.ERRPacket;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.GENERIC_ERR_PACKET)
public class ERRPacketDecoder extends AbsPacketDecoder<ERRPacket> {

    @Override
    public PacketWrapper<ERRPacket> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        // find out if there is a checksum end at tail of bytes
        boolean endWithChecksum = isEndWithChecksum(buf);

        int header = Byte.toUnsignedInt(buf.readByte());
        int errorCode = ByteUtil.readIntAndRelease(buf.readBytes(2));
        String sqlStateMarker = ByteUtil.readStringAndRelease(buf.readBytes(1));
        String sqlState = ByteUtil.readStringAndRelease(buf.readBytes(5));
        String errorMessage;
        if (endWithChecksum){
            errorMessage = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes() - 4));
        } else {
            errorMessage = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes()));
        }
        ERRPacket errPacket = new ERRPacket(packetHeader, header, errorCode, sqlStateMarker, sqlState, errorMessage);

        buf.release();
        return new PacketWrapper<>(errPacket);
    }

}
