package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.DoubleColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_DOUBLE)
public class DoubleColumnDecoder extends AbsColumnDecoder<DoubleColumn> {


    @Override
    public DoubleColumn decode(ByteBuf in, ColumnDef columnDef) {
        double val = ByteUtil.readDoubleAndRelease(in.readBytes(8));
        DoubleColumn doubleColumn = new DoubleColumn();
        doubleColumn.setVal(val);
        return doubleColumn;
    }


}
