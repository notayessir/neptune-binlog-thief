package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.ColumnDecoder;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.Column;
import com.notayessir.processor.decoder.column.StringColumn;
import io.netty.buffer.ByteBuf;

@Decoder(ColumnType.MYSQL_TYPE_STRING)
public class StringColumnDecoder extends AbsColumnDecoder<Column<?>> {



    @Override
    public Column<?> decode(ByteBuf in, ColumnDef columnDef) {
        ColumnType realColumnType = ColumnType.getByVal(Byte.toUnsignedInt(columnDef.getMetadata()[0]));
        // read binary type
        if (realColumnType == ColumnType.MYSQL_TYPE_STRING){
            int len = Byte.toUnsignedInt(in.readByte());
            byte[] bytes = ByteUtil.readBytesAndRelease(in.readBytes(len));
            String val = new String(bytes);
            StringColumn stringColumn = new StringColumn();
            stringColumn.setVal(val);
            return stringColumn;
        }
//        System.out.println("realColumnType:"+realColumnType +" 0:" +Byte.toUnsignedInt(columnDef.getMetadata()[0]) +" metadata:"+ Arrays.toString(columnDef.getMetadata()));
        ColumnDecoder<?> columnDecoder = AbsColumnDecoder.getColumnDecoder(realColumnType);
        return columnDecoder.decode(in, columnDef);
    }


}
