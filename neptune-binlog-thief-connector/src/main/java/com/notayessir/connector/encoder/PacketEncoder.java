package com.notayessir.connector.encoder;

import io.netty.buffer.ByteBuf;

public abstract class PacketEncoder<P> {

    public abstract void encode(P packet, ByteBuf out);

}
