package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.LongColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_LONG)
public class LongColumnDecoder extends AbsColumnDecoder<LongColumn> {


    @Override
    public LongColumn decode(ByteBuf in, ColumnDef columnDef) {
        long val = ByteUtil.readLongAndRelease(in.readBytes(4));
        LongColumn longColumn = new LongColumn();
        longColumn.setVal(val);
        return longColumn;
    }


}
