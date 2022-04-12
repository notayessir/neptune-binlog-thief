package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.ShortColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_SHORT)
public class ShortColumnDecoder extends AbsColumnDecoder<ShortColumn> {



    @Override
    public ShortColumn decode(ByteBuf in, ColumnDef columnDef) {
        int val = ByteUtil.readIntAndRelease(in.readBytes(2));
        ShortColumn shortColumn = new ShortColumn();
        shortColumn.setVal(val);
        return shortColumn;
    }

}
