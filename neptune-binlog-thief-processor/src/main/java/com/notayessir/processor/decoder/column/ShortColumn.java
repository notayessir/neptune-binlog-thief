package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class ShortColumn extends AbsColumn<Integer> {

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_INT24;
    }
}
