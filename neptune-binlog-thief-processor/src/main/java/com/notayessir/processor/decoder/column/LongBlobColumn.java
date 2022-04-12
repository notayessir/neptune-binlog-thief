package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class LongBlobColumn extends AbsColumn<byte[]>{


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_LONG_BLOB;
    }

}
