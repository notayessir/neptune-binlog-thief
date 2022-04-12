package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.common.packet.BinlogDumpPacket;
import com.notayessir.common.packet.PacketType;
import io.netty.buffer.ByteBuf;

@Encoder(PacketType.BINLOG_DUMP_PACKET)
public class BinlogDumpPacketEncoder extends AbsPacketEncoder<BinlogDumpPacket> {

    @Override
    public void encodeBody(BinlogDumpPacket packet, ByteBuf out) {
        out.writeByte(packet.getBinlogDump());
        out.writeBytes(packet.getBinlogPos());
        out.writeBytes(packet.getFlags());
        out.writeBytes(packet.getServerId());
        out.writeBytes(packet.getBinlogFilename().getBytes());
    }
}
