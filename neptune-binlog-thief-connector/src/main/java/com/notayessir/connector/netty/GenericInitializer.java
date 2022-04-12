package com.notayessir.connector.netty;

import com.notayessir.common.configure.ThiefConfiguration;
import com.notayessir.common.packet.PacketType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * netty channel 初始化
 */
public class GenericInitializer extends ChannelInitializer<SocketChannel> {

    private final GenericHandler genericHandler;

    private final DecodeSwitch decodeSwitch;


    public GenericInitializer(ThiefConfiguration thiefConfiguration) {
        decodeSwitch = new DecodeSwitch(PacketType.HANDSHAKE_REQ_PACKET);
        genericHandler = new GenericHandler(decodeSwitch, thiefConfiguration);
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 出栈编码器
        pipeline.addLast("encoder", new GenericEncoder());
        // 入栈解码器
        pipeline.addLast("decoder", new GenericDecoder(decodeSwitch));
        // 入栈处理器
        pipeline.addLast("handler", genericHandler);

    }
}
