package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class JSONColumn extends AbsColumn<byte[]> {



    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_JSON;
    }
}
