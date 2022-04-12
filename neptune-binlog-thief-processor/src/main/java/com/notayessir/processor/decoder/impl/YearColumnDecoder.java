package com.notayessir.processor.decoder.impl;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.column.ColumnType;
import com.notayessir.processor.decoder.Decoder;
import com.notayessir.processor.decoder.column.YearColumn;
import io.netty.buffer.ByteBuf;

/**
 * @see <a href=https://dev.mysql.com/doc/internals/en/date-and-time-data-type-representation.html>date representation</a> /a>
 */
@Decoder(ColumnType.MYSQL_TYPE_YEAR)
public class YearColumnDecoder extends AbsColumnDecoder<YearColumn> {


    @Override
    public YearColumn decode(ByteBuf in, ColumnDef columnDef) {
        // type of YEAR start of 1900
        int year = 1900;
        int val = Byte.toUnsignedInt(in.readByte()) + year;
        YearColumn yearColumn = new YearColumn();
        yearColumn.setVal(val);
        return yearColumn;
    }

}
