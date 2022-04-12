package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.FloatColumn;
import io.netty.buffer.ByteBuf;


@Decoder(ColumnType.MYSQL_TYPE_FLOAT)
public class FloatColumnDecoder extends AbsColumnDecoder<FloatColumn> {


    @Override
    public FloatColumn decode(ByteBuf in, ColumnDef columnDef) {
        float val = ByteUtil.readFloatAndRelease(in.readBytes(4));
        FloatColumn floatColumn = new FloatColumn();
        floatColumn.setVal(val);
        return floatColumn;
    }

}
