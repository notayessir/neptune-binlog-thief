package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class TinyBlobColumn extends AbsColumn<byte[]>{


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_TINY_BLOB;
    }

}
