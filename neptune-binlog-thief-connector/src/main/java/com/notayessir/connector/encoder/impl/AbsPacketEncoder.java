package com.notayessir.connector.encoder.impl;

import com.notayessir.connector.encoder.Encoder;
import com.notayessir.connector.encoder.PacketEncoder;
import com.notayessir.common.packet.Packet;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

/**
 * 抽象数据帧编码器
 * @param <P>   待编码的数据帧
 */
public abstract class AbsPacketEncoder<P extends Packet> extends PacketEncoder<P> {


    /**
     * 对数据帧的头部进行编码的公共方法
     * @param packet    数据帧
     * @param out       字节流
     */
    protected void encodePacketHeader(P packet, ByteBuf out){
        PacketHeader packetHeader = packet.getPacketHeader();
        out.writeBytes(ByteUtil.readPayloadLen(packetHeader.getPayloadLength()));
        out.writeByte(packetHeader.getSequenceId());
    }


    @Override
    public void encode(P packet, ByteBuf out) {
        encodePacketHeader(packet, out);
        encodeBody(packet, out);
    }

    /**
     * 对数据帧的 body 部分进行编码
     * @param packet    数据帧
     * @param out       字节流
     */
    public abstract void encodeBody(P packet, ByteBuf out);


    /**
     * 用一个 map 来保存所有数据帧的编码器
     */
    private static final HashMap<PacketType, PacketEncoder<Object>> encoders = new HashMap<>();

    /**
     * 编码器所在路径
     */
    private final static String PACKAGE_NAME = AbsPacketEncoder.class.getPackage().getName();

    static {
        // 使用反射将数据帧编码器实例化出来
        Reflections reflections = new Reflections(PACKAGE_NAME);
        try {
            Set<Class<?>> clz = reflections.getTypesAnnotatedWith(Encoder.class);
            for (Class<?> writer : clz){
                Encoder annotation = writer.getAnnotation(Encoder.class);
                encoders.put(annotation.value(), (PacketEncoder<Object>) writer.newInstance());
            }
        }catch (Exception e){
            throw new RuntimeException("error happened when scan packet reader ");
        }
    }

    /**
     * 根据数据帧类型获取对应的编码器
     * @param packetType    数据帧类型
     * @return              编码器
     */
    public static PacketEncoder<Object> getPacketEncoder(PacketType packetType){
        return encoders.get(packetType);
    }


}
