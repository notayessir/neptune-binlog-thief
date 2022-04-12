package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.Time2Column;
import com.notayessir.processor.util.FSPUtil;
import io.netty.buffer.ByteBuf;

import java.time.LocalTime;

@Decoder(ColumnType.MYSQL_TYPE_TIME2)
public class Time2ColumnDecoder extends AbsColumnDecoder<Time2Column> {



    @Override
    public Time2Column decode(ByteBuf in, ColumnDef columnDef) {
        byte[] integerBytes = ByteUtil.readBytesAndRelease(in.readBytes(3));
        byte fractionLen = columnDef.getMetadata()[0];
        if (fractionLen != 0){
            byte[] fractionBytes = ByteUtil.readBytesAndRelease(in.readBytes(FSPUtil.getLen(fractionLen)));
        }


        String binary = ByteUtil.bytesToBinaryString(integerBytes);
        int hour = Integer.parseInt(binary.substring(2, 12), 2);
        int minute = Integer.parseInt(binary.substring(12, 18), 2);
        int second = Integer.parseInt(binary.substring(18), 2);
        LocalTime val = LocalTime.of(hour, minute, second);
        Time2Column time2Column = new Time2Column();
        time2Column.setVal(val);
        return time2Column;
    }


}
