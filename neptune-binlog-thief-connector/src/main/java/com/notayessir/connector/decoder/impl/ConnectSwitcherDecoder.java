package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.connector.decoder.PacketDecoder;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.CONNECT_SWITCHER)
public class ConnectSwitcherDecoder extends AbsPacketDecoder<Object> {


    @Override
    public PacketWrapper<Object> decodePacket(ByteBuf in) {
        int rIndex = in.readerIndex();
        PacketHeader packetHeader = decodePacketHeader(in);
        int header = Byte.toUnsignedInt(in.readByte());
        in.readerIndex(rIndex);

        PacketDecoder<?> packetDecoder;
        PacketWrapper<Object> packetWrapper;

        if (header == 0xff){
            // switch to GENERIC_ERR_PACKET
            packetDecoder = getPacketDecoder(PacketType.GENERIC_ERR_PACKET);
        } else if (header == 0x00){
            // switch to GENERIC_OK_PACKET
            packetDecoder = getPacketDecoder(PacketType.GENERIC_OK_PACKET);
        } else if (header == 0xfe && packetHeader.getPayloadLength() == 5){
            // switch to EOF_PACKET
            packetDecoder = getPacketDecoder(PacketType.GENERIC_EOF_PACKET);
        } else {
            // switch to AUTH_SWITCH_REQ_PACKET, header == (byte) 0xfe
            packetDecoder = getPacketDecoder(PacketType.AUTH_SWITCH_REQ_PACKET);
        }
        packetWrapper = (PacketWrapper<Object>) packetDecoder.decode(in);
        return packetWrapper;
    }
}
