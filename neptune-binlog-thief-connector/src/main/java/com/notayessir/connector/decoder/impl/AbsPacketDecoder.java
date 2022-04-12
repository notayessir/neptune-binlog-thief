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

public abstract class AbsPacketDecoder<P> implements PacketDecoder<PacketWrapper<P>> {


    protected boolean isByteNotEnough(ByteBuf in){
        if (in.readableBytes() < 4) {
            return true;
        }
        int rIndex = in.readerIndex();
        int payloadLen = ByteUtil.readIntAndRelease(in.readBytes(3));
        // +1 represent sequence byte
        if (in.readableBytes() < payloadLen + 1){
            in.readerIndex(rIndex);
            return true;
        }
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


    public abstract PacketWrapper<P> decodePacket(ByteBuf in);


    protected PacketHeader decodePacketHeader(ByteBuf in){
        int payloadLen = ByteUtil.readIntAndRelease(in.readBytes(3));
        int sequenceId = Byte.toUnsignedInt(in.readByte());
        return new PacketHeader(payloadLen, sequenceId);
    }


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

    protected boolean isEndWithChecksum(ByteBuf buf){
        int readerIndex = buf.readerIndex();
        // skip 1 byte of ok
        buf.skipBytes(1);
        byte [] bytes = new byte[buf.readableBytes() - 4];
        buf.readBytes(bytes);

        byte [] crc32 = new byte[4];
        buf.readBytes(crc32);

        boolean endWithCRC32 = false;
        if (CRC32Util.CRC32(bytes) == ByteUtil.readLong(crc32)){
            endWithCRC32 = true;
        }
        buf.readerIndex(readerIndex);
        return endWithCRC32;
    }




    private static final HashMap<PacketType, AbsPacketDecoder<?>> packetDecoderMap = new HashMap<>();

    private final static String PACKAGE_NAME = AbsPacketDecoder.class.getPackage().getName();



    static {
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


    public static AbsPacketDecoder<?> getPacketDecoder(PacketType packetType){
        return packetDecoderMap.get(packetType);
    }



}
