package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.GENERIC_WAITING)
public class WaitingPacketDecoder extends AbsPacketDecoder<Void> {

    @Override
    public PacketWrapper<Void> decodePacket(ByteBuf in) {
        return new PacketWrapper<>();
    }

}
