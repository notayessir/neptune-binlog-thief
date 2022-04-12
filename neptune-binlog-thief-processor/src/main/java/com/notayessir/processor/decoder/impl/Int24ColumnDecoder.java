package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.Int24Column;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_INT24)
public class Int24ColumnDecoder extends AbsColumnDecoder<Int24Column> {


    @Override
    public Int24Column decode(ByteBuf in, ColumnDef columnDef) {
        int val = ByteUtil.readIntAndRelease(in.readBytes(3));
        Int24Column int24Column = new Int24Column();
        int24Column.setVal(val);
        return int24Column;
    }


}
