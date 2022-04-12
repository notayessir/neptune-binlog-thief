package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.PreviousGTIDSEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.PREVIOUS_GTIDS_EVENT)
public class PreviousGTIDSEventDecoder extends AbsPacketDecoder<PreviousGTIDSEvent> {


    @Override
    public PacketWrapper<PreviousGTIDSEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());
        EventHeader eventHeader = decodeEventHeader(buf);

        // TODO: figure out meaning of data
        byte [] data = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        PreviousGTIDSEvent event = new PreviousGTIDSEvent(packetHeader, eventHeader, data);

        buf.release();
        return new PacketWrapper<>(event);
    }

}
