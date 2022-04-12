package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.VarcharColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_VARCHAR)
public class VarcharColumnDecoder extends AbsColumnDecoder<VarcharColumn> {


    @Override
    public VarcharColumn decode(ByteBuf in, ColumnDef columnDef) {
        int len = ByteUtil.readEncodedInt(in);
        int precision = Byte.toUnsignedInt(in.readByte());
        String val = ByteUtil.readStringAndRelease(in.readBytes(len));
        VarcharColumn varcharColumn = new VarcharColumn();
        varcharColumn.setVal(val);
        return varcharColumn;
    }

}
