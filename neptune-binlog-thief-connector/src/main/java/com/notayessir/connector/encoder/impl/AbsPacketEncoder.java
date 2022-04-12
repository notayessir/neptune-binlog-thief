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

public abstract class AbsPacketEncoder<P extends Packet> extends PacketEncoder<P> {

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

    public abstract void encodeBody(P packet, ByteBuf out);



    private static final HashMap<PacketType, PacketEncoder<Object>> encoders = new HashMap<>();

    private final static String PACKAGE_NAME = AbsPacketEncoder.class.getPackage().getName();

    static {
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


    public static PacketEncoder<Object> getPacketEncoder(PacketType packetType){
        return encoders.get(packetType);
    }


}
