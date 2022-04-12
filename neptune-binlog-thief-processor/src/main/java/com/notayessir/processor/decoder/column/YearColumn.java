package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class YearColumn extends AbsColumn<Integer>{

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_YEAR;
    }
}
