package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class DoubleColumn extends AbsColumn<Double>{

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_DOUBLE;
    }

}
