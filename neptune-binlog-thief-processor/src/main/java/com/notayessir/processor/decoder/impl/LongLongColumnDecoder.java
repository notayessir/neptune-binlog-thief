package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.LongLongColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_LONGLONG)
public class LongLongColumnDecoder extends AbsColumnDecoder<LongLongColumn> {


    @Override
    public LongLongColumn decode(ByteBuf in, ColumnDef columnDef) {
        long val = ByteUtil.readLongAndRelease(in.readBytes(8));
        LongLongColumn longLongColumn = new LongLongColumn();
        longLongColumn.setVal(val);
        return longLongColumn;
    }



}
