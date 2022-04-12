package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.XidEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

@Decoder(PacketType.XID_EVENT)
public class XidEventDecoder extends AbsPacketDecoder<XidEvent> {

    @Override
    public PacketWrapper<XidEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

        EventHeader eventHeader = decodeEventHeader(buf);
        byte [] data = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        if (data.length != 8){
            data = Arrays.copyOfRange(data, 0, 8);
        }
        long xid = ByteUtil.readLong(data);
        XidEvent event = new XidEvent(packetHeader, eventHeader, xid);

        buf.release();
        return new PacketWrapper<>(event);
    }


}
