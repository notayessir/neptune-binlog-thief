package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.VarStringColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_VAR_STRING)
public class VarStringColumnDecoder extends AbsColumnDecoder<VarStringColumn> {


    @Override
    public VarStringColumn decode(ByteBuf in, ColumnDef columnDef) {
        int len = ByteUtil.readEncodedInt(in);
        String val = ByteUtil.readStringAndRelease(in.readBytes(len));
        VarStringColumn varStringColumn = new VarStringColumn();
        varStringColumn.setVal(val);
        return varStringColumn;
    }

}
