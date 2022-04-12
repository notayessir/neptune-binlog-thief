package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class Int24Column extends AbsColumn<Integer>{



    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_INT24;
    }
}
