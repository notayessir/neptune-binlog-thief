package com.notayessir.connector.decoder.impl;

import com.notayessir.connector.decoder.Decoder;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.QueryEvent;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;

@Decoder(PacketType.QUERY_EVENT)
public class QueryEventDecoder extends AbsPacketDecoder<QueryEvent> {


    @Override
    public PacketWrapper<QueryEvent> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());
        // find out if there is a checksum end at tail of bytes
        boolean endWithChecksum = isEndWithChecksum(buf);

        EventHeader eventHeader = decodeEventHeader(buf);
        int slaveProxyId = ByteUtil.readIntAndRelease(buf.readBytes(4));
        int executionTime = ByteUtil.readIntAndRelease(buf.readBytes(4));
        int schemaLength = Byte.toUnsignedInt(buf.readByte());
        int errorCode = ByteUtil.readIntAndRelease(buf.readBytes(2));
        int statusVarsLength = ByteUtil.readIntAndRelease(buf.readBytes(2));
        byte [] statusVars = ByteUtil.readBytesAndRelease(buf.readBytes(statusVarsLength));
        String schema = ByteUtil.readStringAndRelease(buf.readBytes(schemaLength));
        byte skip = buf.readByte();
        String query;
        if (endWithChecksum){
            query = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes() - 4));
        }else {
            query = ByteUtil.readStringAndRelease(buf.readBytes(buf.readableBytes()));
        }
        QueryEvent event = new QueryEvent(packetHeader, eventHeader, slaveProxyId, executionTime, errorCode,
                statusVars, schema, query);
        buf.release();
        return new PacketWrapper<>(event);
    }

}
