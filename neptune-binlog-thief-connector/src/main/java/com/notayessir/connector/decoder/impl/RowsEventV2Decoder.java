package com.notayessir.connector.decoder.impl;

import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.PacketWrapper;
import com.notayessir.common.packet.event.RowsEventV2;
import com.notayessir.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;


public class RowsEventV2Decoder extends AbsPacketDecoder<RowsEventV2> {

    @Override
    public PacketWrapper<RowsEventV2> decodePacket(ByteBuf in) {
        PacketHeader packetHeader = decodePacketHeader(in);
        ByteBuf buf = in.readBytes(packetHeader.getPayloadLength());

//        System.out.println(JSONObject.toJSONString(packetHeader));
//        byte [] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        System.out.println(Arrays.toString(bytes));
//        buf = Unpooled.wrappedBuffer(bytes);


        EventHeader eventHeader = decodeEventHeader(buf);
        long tableId = ByteUtil.readLongAndRelease(buf.readBytes(6));
        int flags = ByteUtil.readIntAndRelease(buf.readBytes(2));
        int extraDataLength = ByteUtil.readIntAndRelease(buf.readBytes(2));
        if (extraDataLength - 2 > 0){
            byte[] extraData = ByteUtil.readBytesAndRelease(buf.readBytes(extraDataLength));
        }
        int columnNum = ByteUtil.readEncodedInt(buf);
        byte [] columnsPresentBitmap = ByteUtil.readBytesAndRelease(buf.readBytes((columnNum + 7) / 8));

        // update event, MySQL version should greater than 5.6x
        byte [] columnsPresentBitmapForUpdate = null;
        PacketType type = PacketType.getByVal(eventHeader.getEventType());
        if (type == PacketType.UPDATE_ROWS_EVENTv2){
            columnsPresentBitmapForUpdate = ByteUtil.readBytesAndRelease(buf.readBytes((columnNum + 7) / 8));
        }

        byte [] rowsData = ByteUtil.readBytesAndRelease(buf.readBytes(buf.readableBytes()));
        RowsEventV2 event = new RowsEventV2(packetHeader, eventHeader, tableId, flags, columnNum,
                columnsPresentBitmap, columnsPresentBitmapForUpdate, rowsData);
        buf.release();
        return new PacketWrapper<>(event);
    }

}
