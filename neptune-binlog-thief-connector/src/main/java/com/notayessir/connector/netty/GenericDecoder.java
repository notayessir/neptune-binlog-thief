package com.notayessir.connector.netty;

import com.notayessir.connector.decoder.PacketDecoder;
import com.notayessir.connector.decoder.impl.AbsPacketDecoder;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * binlog 解码器，将字节流解码成对应的帧
 */
public class GenericDecoder extends ByteToMessageDecoder {


    private final DecodeSwitch decodeSwitch;

    public GenericDecoder(DecodeSwitch decodeSwitch) {
        this.decodeSwitch = decodeSwitch;
    }


    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketType packetType = decodeSwitch.getNextPacketType();
        PacketDecoder<?> packetDecoder = AbsPacketDecoder.getPacketDecoder(packetType);
        PacketWrapper<?> packetWrapper = (PacketWrapper<?>) packetDecoder.decode(in);
        if (packetWrapper.isCompleted()){
            out.add(packetWrapper.getPacket());
            decodeSwitch.setNextPacketType(PacketType.GENERIC_WAITING);
        }
    }


}
