package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.TinyColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_TINY)
public class TinyColumnDecoder extends AbsColumnDecoder<TinyColumn> {



    @Override
    public TinyColumn decode(ByteBuf in, ColumnDef columnDef) {
        int val = Byte.toUnsignedInt(in.readByte());
        TinyColumn tinyColumn = new TinyColumn();
        tinyColumn.setVal(val);
        return tinyColumn;
    }

}
