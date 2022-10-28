package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.connector.decoder.PacketDecoder;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.BINLOG_SWITCHER)
public class BinlogSwitcherDecoder extends AbsPacketDecoder<Object> {


    @Override
    public PacketWrapper<Object> decodePacket(ByteBuf in) {
        int rIndex = in.readerIndex();
        PacketHeader packetHeader = decodePacketHeader(in);
        int flag = Byte.toUnsignedInt(in.readByte());
        PacketDecoder<?> packetDecoder;
        PacketWrapper<Object> packetWrapper;

        // switch to GENERIC_ERR_PACKET
        if (flag == 0xff){
            in.readerIndex(rIndex);
            packetDecoder = getPacketDecoder(PacketType.GENERIC_ERR_PACKET);
            packetWrapper = (PacketWrapper<Object>) packetDecoder.decode(in);
            return packetWrapper;
        }

        if (flag == 0xfe && packetHeader.getPayloadLength() == 5){
            in.readerIndex(rIndex);
            packetDecoder = getPacketDecoder(PacketType.GENERIC_EOF_PACKET);
            packetWrapper = (PacketWrapper<Object>) packetDecoder.decode(in);
            return packetWrapper;
        }

        // flag == 0x00
        // skip timestamp bytes
        in.skipBytes(4);
        int eventType = Byte.toUnsignedInt(in.readByte());
        in.readerIndex(rIndex);
        // 根据数据帧细分类型，选择对应的解码器
        PacketType type = PacketType.getByVal(eventType);
        packetDecoder = getPacketDecoder(type);
        return (PacketWrapper<Object>) packetDecoder.decode(in);
    }
}
