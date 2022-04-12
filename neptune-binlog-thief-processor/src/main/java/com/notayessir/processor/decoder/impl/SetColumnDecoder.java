package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.SetColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_SET)
public class SetColumnDecoder extends AbsColumnDecoder<SetColumn> {


    @Override
    public SetColumn decode(ByteBuf in, ColumnDef columnDef) {
        int fieldSize = Byte.toUnsignedInt(columnDef.getMetadata()[1]);
        int index = ByteUtil.readIntAndRelease(in.readBytes(fieldSize));
        // TODO get value from index
        String val = "" + index;
        SetColumn setColumn = new SetColumn();
        setColumn.setVal(val);
        return setColumn;
    }

}
