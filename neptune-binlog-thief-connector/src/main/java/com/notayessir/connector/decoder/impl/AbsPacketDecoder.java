package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.connector.decoder.PacketDecoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.common.util.CRC32Util;
import io.netty.buffer.ByteBuf;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

/**
 * 抽象数据帧解码器，包含一些公共方法
 * @param <P>   数据帧类型
 */
public abstract class AbsPacketDecoder<P> implements PacketDecoder<PacketWrapper<P>> {

    /**
     * 检查字节流长度是否能够解析成一个数据帧
     * @param in    网络字节流
     * @return      字节流长度是否足够
     */
    protected boolean isByteNotEnough(ByteBuf in){
        // 3 字节 payload 长度 + 1 字节序列号
        if (in.readableBytes() < 4) {
            return true;
        }
        int rIndex = in.readerIndex();
        // #1
        int payloadLen = ByteUtil.readIntAndRelease(in.readBytes(3));
        // 检查字节流中 payload 长度是否足够
        if (in.readableBytes() < payloadLen + 1){
            in.readerIndex(rIndex);
            return true;
        }
        // 不够的话将 #1 读取字节位置重置
        in.readerIndex(rIndex);
        return false;
    }



    @Override
    public PacketWrapper<P> decode(ByteBuf in) {
        PacketWrapper<P> packetWrapper = new PacketWrapper<>();
        if (isByteNotEnough(in)){
            return packetWrapper;
        }
        return decodePacket(in);
    }

    /**
     * 读出字节，并组成数据帧
     * @param in    字节流
     * @return      数据帧
     */
    public abstract PacketWrapper<P> decodePacket(ByteBuf in);


    /**
     * 读取数据帧头部，根据官网文档，数据帧头部的数据结构是一样的
     * @param in    字节流
     * @return      数据帧头部
     */
    protected PacketHeader decodePacketHeader(ByteBuf in){
        int payloadLen = ByteUtil.readIntAndRelease(in.readBytes(3));
        int sequenceId = Byte.toUnsignedInt(in.readByte());
        return new PacketHeader(payloadLen, sequenceId);
    }

    /**
     * 读取事件帧头部，根据官方文档，事件帧头部的数据结构是一样的
     * @param in    数据流
     * @return      事件帧头部
     */
    protected EventHeader decodeEventHeader(ByteBuf in){
        byte ok = in.readByte();
        long timestamp = ByteUtil.readLongAndRelease(in.readBytes(4));
        byte eventType = in.readByte();
        int serverId = ByteUtil.readIntAndRelease(in.readBytes(4));
        int eventSize = ByteUtil.readIntAndRelease(in.readBytes(4));
        int pos = ByteUtil.readIntAndRelease(in.readBytes(4));
        int flags = ByteUtil.readIntAndRelease(in.readBytes(2));
        return new EventHeader(timestamp, eventType, serverId, eventSize, pos, flags);
    }


    /**
     * 使用 crc32 检查数据帧的字节流是否有误
     * @param buf   字节流
     * @return      数据帧是否有误
     */
    protected boolean isEndWithChecksum(ByteBuf buf){
        int readerIndex = buf.readerIndex();
        // skip 1 byte of ok
        buf.skipBytes(1);
        byte [] bytes = new byte[buf.readableBytes() - 4];
        buf.readBytes(bytes);

        byte [] crc32 = new byte[4];
        buf.readBytes(crc32);
        buf.readerIndex(readerIndex);
        return CRC32Util.CRC32(bytes) == ByteUtil.readLong(crc32);
    }


    /**
     * 同一个 map 保存所有数据帧解码器
     */
    private static final HashMap<PacketType, AbsPacketDecoder<?>> packetDecoderMap = new HashMap<>();

    /**
     * 解码器路径，下面使用反射获取实例
     */
    private final static String PACKAGE_NAME = AbsPacketDecoder.class.getPackage().getName();


    static {
        // 使用反射获取所有解码器实例
        Reflections reflections = new Reflections(PACKAGE_NAME);
        try {
            Set<Class<?>> clz = reflections.getTypesAnnotatedWith(Decoder.class);
            for (Class<?> decoder : clz){
                Decoder annotation = decoder.getAnnotation(Decoder.class);
                packetDecoderMap.put(annotation.value(), (AbsPacketDecoder<?>) decoder.newInstance());
            }
        }catch (Exception e){
            throw new RuntimeException("error happened when scan packet decoder.");
        }
    }

    /**
     * 根据数据帧类型获取对应的解码器
     * @param packetType    数据帧类型
     * @return              解码器
     */
    public static AbsPacketDecoder<?> getPacketDecoder(PacketType packetType){
        return packetDecoderMap.get(packetType);
    }



}
