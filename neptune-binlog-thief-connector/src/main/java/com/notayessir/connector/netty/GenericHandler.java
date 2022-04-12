package com.notayessir.connector.netty;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.connector.EventPublisher;
import com.notayessir.common.AppContextProvider;
import com.notayessir.common.configure.ThiefConfiguration;
import com.notayessir.common.packet.*;
import com.notayessir.common.util.AuthUtil;
import com.notayessir.common.util.ByteUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 帧处理器
 */
public class GenericHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(GenericHandler.class);


    private final DecodeSwitch decodeSwitch;

    private final ThiefConfiguration thiefConfiguration;

    public GenericHandler(DecodeSwitch decodeSwitch, ThiefConfiguration thiefConfiguration) {
        this.decodeSwitch = decodeSwitch;
        this.thiefConfiguration = thiefConfiguration;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("binlog deliverer channelActive.");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 响应回复
        handle(msg, ctx);
        // 将 binlog 事件发布
        EventPublisher publisher = AppContextProvider.getBean(EventPublisher.class);
        publisher.publish(new BinlogPacket(msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("binlog deliverer exceptionCaught.", cause);
        cause.printStackTrace();
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOG.info("binlog deliverer channelUnregistered.");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("binlog deliverer channelInactive.");
    }


    /**
     * 处理服务器的响应
     * @param msg   服务器响应的帧
     * @param ctx   tcp channel
     */
    public void handle(Object msg, ChannelHandlerContext ctx) {
        Packet packet = (Packet) msg;
        PacketType packetType = packet.getPacketType();
        switch (packetType){
            case AUTH_SWITCH_REQ_PACKET:
                handleAuthSwitchReqPacket(msg, ctx);
                break;
            case GENERIC_EOF_PACKET:
                handleEOFPacket(msg, ctx);
                break;
            case GENERIC_ERR_PACKET:
                handleERRPacket(msg, ctx);
                break;
            case HANDSHAKE_REQ_PACKET:
                handleHandshakeReqPacket(msg, ctx);
                break;
            case GENERIC_OK_PACKET:
                handleOKPacket(msg, ctx);
                break;
            default:
                decodeSwitch.setNextPacketType(PacketType.BINLOG_SWITCHER);
                break;

        }
    }

    /**
     * 响应 AuthSwitchReqPacket
     * @param msg   AuthSwitchReqPacket
     * @param ctx   channel
     */
    private void handleAuthSwitchReqPacket(Object msg, ChannelHandlerContext ctx){
        decodeSwitch.setNextPacketType(PacketType.CONNECT_SWITCHER);
        decodeSwitch.setNextAction(DecodeAction.CLOSE_CHECKSUM);
        AuthSwitchReqPacket authSwitchReqPacket = (AuthSwitchReqPacket) msg;
        byte[] authResponse = AuthUtil.nativePassword(thiefConfiguration.getPassword().getBytes(), authSwitchReqPacket.getAuthPluginData());
        AuthSwitchRespPacket switchResponsePacket = new AuthSwitchRespPacket(new PacketHeader((byte) (authSwitchReqPacket.getPacketHeader().getSequenceId() + 1)), authResponse);
        ctx.writeAndFlush(switchResponsePacket);
    }

    /**
     * 响应 HandshakeReqPacket
     * @param msg   HandshakeReqPacket
     * @param ctx   channel
     */
    private void handleHandshakeReqPacket(Object msg, ChannelHandlerContext ctx){
        decodeSwitch.setNextPacketType(PacketType.CONNECT_SWITCHER);
        decodeSwitch.setNextAction(DecodeAction.CLOSE_CHECKSUM);
        HandshakeReqPacket handshakeReqPacket = (HandshakeReqPacket) msg;
        byte[] authResponse = AuthUtil.nativePassword(thiefConfiguration.getPassword().getBytes(), handshakeReqPacket.getAuthPluginData());
        HandshakeRespPacket handshakePacket = new HandshakeRespPacket(new PacketHeader((byte) (handshakeReqPacket.getPacketHeader().getSequenceId() + 1)),
                thiefConfiguration.getUsername().getBytes(), authResponse);
        ctx.writeAndFlush(handshakePacket);
    }

    /**
     * 响应 EOFPacket
     * @param msg   EOFPacket
     * @param ctx   channel
     */
    private void handleEOFPacket(Object msg, ChannelHandlerContext ctx){
        LOG.info("response by an eof packet, close the channel : {}", JSONObject.toJSONString(msg));
        ctx.channel().close();
    }

    /**
     * 响应 ERRPacket
     * @param msg   ERRPacket
     * @param ctx   channel
     */
    private void handleERRPacket(Object msg, ChannelHandlerContext ctx){
        LOG.info("response by an error packet, close the channel : {}", JSONObject.toJSONString(msg));
        ctx.channel().close();
    }

    /**
     * 响应 OKPacket
     * @param msg   OKPacket
     * @param ctx   channel
     */
    private void handleOKPacket(Object msg, ChannelHandlerContext ctx){
        if (decodeSwitch.getNextAction() == DecodeAction.CLOSE_CHECKSUM){
            decodeSwitch.setNextPacketType(PacketType.GENERIC_OK_PACKET);
            decodeSwitch.setNextAction(DecodeAction.REGISTER_SLAVE);
            ComQueryPacket comQueryPacket = new ComQueryPacket(new PacketHeader(0), "set @master_binlog_checksum=@@global.binlog_checksum");
            ctx.writeAndFlush(comQueryPacket);
        } else if (decodeSwitch.getNextAction() == DecodeAction.REGISTER_SLAVE){
            decodeSwitch.setNextPacketType(PacketType.CONNECT_SWITCHER);
            decodeSwitch.setNextAction(DecodeAction.BINLOG_DUMP);
            RegisterSlavePacket registerSlavePacket = new RegisterSlavePacket(new PacketHeader(0), 65535);
            ctx.writeAndFlush(registerSlavePacket);
        } else if (decodeSwitch.getNextAction() == DecodeAction.BINLOG_DUMP){
            decodeSwitch.setNextPacketType(PacketType.BINLOG_SWITCHER);
            decodeSwitch.setNextAction(DecodeAction.NONE);
            BinlogDumpPacket binlogDumpPacket =
                    new BinlogDumpPacket(new PacketHeader(0), ByteUtil.intToBytes(thiefConfiguration.getBinlogStartPos().intValue()),
                    65535, thiefConfiguration.getBinlogFilename());
            ctx.writeAndFlush(binlogDumpPacket);
        }
    }




}
