package com.notayessir.connector.decoder;


import io.netty.buffer.ByteBuf;

public interface PacketDecoder<P> {

    P decode(ByteBuf in);

}
