package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.DateTime2Column;
import com.notayessir.processor.util.FSPUtil;
import io.netty.buffer.ByteBuf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Decoder(ColumnType.MYSQL_TYPE_DATETIME2)
public class DateTime2ColumnDecoder extends AbsColumnDecoder<DateTime2Column> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public DateTime2Column decode(ByteBuf in, ColumnDef columnDef) {
        byte[] bytes = ByteUtil.readBytesAndRelease(in.readBytes(5));

        byte fractionLen = columnDef.getMetadata()[0];
        if (fractionLen != 0){
            byte[] fractionBytes = ByteUtil.readBytesAndRelease(in.readBytes(FSPUtil.getLen(fractionLen)));
        }

        String binary = ByteUtil.bytesToBinaryString(bytes);
        int encodedYearAndMon = Integer.parseInt(binary.substring(1, 18), 2);
        int year = encodedYearAndMon / 13;
        int mon = encodedYearAndMon % 13;
        int day = Integer.parseInt(binary.substring(18, 23), 2);
        int hour = Integer.parseInt(binary.substring(23, 28), 2);
        int minute = Integer.parseInt(binary.substring(28, 34), 2);
        int second = Integer.parseInt(binary.substring(34), 2);
        String dateStr = year + "-" + mon + "-" + day + " " + hour + ":" + minute + ":" + second;
        Date val;
        try {
            val = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("ParseException happened in " + this.getClass().getName() + e);
            e.printStackTrace();
            val = null;
        }
        DateTime2Column dateTime2Column = new DateTime2Column();
        dateTime2Column.setVal(val);
        return dateTime2Column;
    }




}
