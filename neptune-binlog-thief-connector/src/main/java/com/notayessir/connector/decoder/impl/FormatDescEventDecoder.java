package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.FormatDescEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.FORMAT_DESCRIPTION_EVENT)
public class FormatDescEventDecoder extends AbsPacketDecoder<FormatDescEvent> {


    @Override
    public PacketWrapper<FormatDescEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        EventHeader eventHeader = decodeEventHeader(buf);
        int binlogVersion = ByteUtil.readIntAndRelease(buf.readBytes(2));
        String mysqlServerVersion = ByteUtil.readStringAndRelease(buf.readBytes(50));
        int timestamp = ByteUtil.readIntAndRelease(buf.readBytes(4));
        int eventHeaderLen = Byte.toUnsignedInt(buf.readByte());
        byte [] eventTypeHeaderLen = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        FormatDescEvent event = new FormatDescEvent(packetHeader, eventHeader, binlogVersion, mysqlServerVersion, timestamp, eventHeaderLen, eventTypeHeaderLen);

        buf.release();
        return new PacketWrapper<>(event);
    }

}
