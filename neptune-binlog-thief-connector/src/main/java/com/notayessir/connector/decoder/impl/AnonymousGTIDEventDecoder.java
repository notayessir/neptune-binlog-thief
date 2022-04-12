package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.AnonymousGTIDEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.ANONYMOUS_GTID_EVENT)
public class AnonymousGTIDEventDecoder extends AbsPacketDecoder<AnonymousGTIDEvent> {


    @Override
    public PacketWrapper<AnonymousGTIDEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        EventHeader eventHeader = decodeEventHeader(buf);
        int GTIDFlags = Byte.toUnsignedInt(buf.readByte());
        byte [] sid = ByteUtil.readBytesAndRelease(buf.readBytes(16));
        byte [] gno = ByteUtil.readBytesAndRelease(buf.readBytes(8));
        int logicTS = Byte.toUnsignedInt(buf.readByte());
        long lastCommitted = ByteUtil.readLongAndRelease(buf.readBytes(8));
        long sequenceNumber = ByteUtil.readLongAndRelease(buf.readBytes(8));
        long immediateCommitTimestamp = ByteUtil.readLongAndRelease(buf.readBytes(7));
        long transactionLength = ByteUtil.readEncodedInt(buf);
        int immediateServerVersion = ByteUtil.readIntAndRelease(buf.readBytes(4));
        byte [] other = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        AnonymousGTIDEvent event = new AnonymousGTIDEvent(packetHeader, eventHeader, GTIDFlags, sid, gno, logicTS, lastCommitted,
                sequenceNumber, immediateCommitTimestamp, transactionLength, immediateServerVersion, other);

        buf.release();
        return new PacketWrapper<>(event);
    }

}
