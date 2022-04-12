package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.TinyBlobColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_TINY_BLOB)
public class TinyBlobColumnDecoder extends AbsColumnDecoder<TinyBlobColumn> {



    @Override
    public TinyBlobColumn decode(ByteBuf in, ColumnDef columnDef) {
        int len = ByteUtil.readEncodedInt(in);
        byte[] val = ByteUtil.readBytesAndRelease(in.readBytes(len));
        TinyBlobColumn tinyBlobColumn = new TinyBlobColumn();
        tinyBlobColumn.setVal(val);
        return tinyBlobColumn;
    }

}
