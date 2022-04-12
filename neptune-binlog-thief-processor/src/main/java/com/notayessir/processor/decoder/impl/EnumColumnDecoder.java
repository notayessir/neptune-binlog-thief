package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.EnumColumn;
import io.netty.buffer.ByteBuf;


@Decoder(ColumnType.MYSQL_TYPE_ENUM)
public class EnumColumnDecoder extends AbsColumnDecoder<EnumColumn> {


    @Override
    public EnumColumn decode(ByteBuf in, ColumnDef columnDef) {
        int fieldSize = Byte.toUnsignedInt(columnDef.getMetadata()[1]);
        int index = ByteUtil.readIntAndRelease(in.readBytes(fieldSize));
        // TODO get value from index
        String val = "" + index;
        EnumColumn enumColumn = new EnumColumn();
        enumColumn.setVal(val);
        return enumColumn;
    }



}
