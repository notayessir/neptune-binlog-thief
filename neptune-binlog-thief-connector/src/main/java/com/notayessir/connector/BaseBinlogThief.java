package com.notayessir.connector;

import com.notayessir.connector.netty.GenericInitializer;
import com.notayessir.common.configure.ThiefConfiguration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


/**
 * 基础 binlog slave 实现
 */
public class BaseBinlogThief implements BinlogThief {


    private static final Logger LOG = LoggerFactory.getLogger(BaseBinlogThief.class);

    private final ThiefConfiguration thiefConfiguration;

    private ChannelFuture channelFuture;

    public BaseBinlogThief(ThiefConfiguration thiefConfiguration) {
        this.thiefConfiguration = thiefConfiguration;
    }


    @Override
    public void start()  {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new GenericInitializer(thiefConfiguration));
        try {
            channelFuture = bootstrap
                    .connect(thiefConfiguration.getHost(), thiefConfiguration.getPort())
                    .sync();
        } catch (InterruptedException e) {
            LOG.info("fail to connect to mysql, ex:", e);
            e.printStackTrace();
        }
//        channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void stop() {
        if (Objects.isNull(channelFuture)){
            return;
        }
        channelFuture.channel().close();
    }
}
