package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.DecimalColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_DECIMAL)
public class DecimalColumnDecoder extends AbsColumnDecoder<DecimalColumn> {


    @Override
    public DecimalColumn decode(ByteBuf in, ColumnDef columnDef) {
        int len = ByteUtil.readEncodedInt(in);
        String val = ByteUtil.readStringAndRelease(in.readBytes(len));
        DecimalColumn decimalColumn = new DecimalColumn();
        decimalColumn.setVal(val);
        return decimalColumn;
    }



}
