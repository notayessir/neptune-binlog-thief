package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.DateColumn;
import io.netty.buffer.ByteBuf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Decoder(ColumnType.MYSQL_TYPE_DATE)
public class DateColumnDecoder extends AbsColumnDecoder<DateColumn> {


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public DateColumn decode(ByteBuf in, ColumnDef columnDef) {
        int sum = ByteUtil.readIntAndRelease(in.readBytes(3));
        int year = sum / (16 * 32);
        int month = (sum % (16 * 32)) / 32;
        int day = (sum % (16 * 32)) % 32;
        String dateStr = year + "-" + month + "-" + day;
        Date val;
        try {
            val = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("ParseException happened in " + this.getClass().getName() + e);
            e.printStackTrace();
            val = null;
        }
        DateColumn dateColumn = new DateColumn();
        dateColumn.setVal(val);
        return dateColumn;
    }


}
