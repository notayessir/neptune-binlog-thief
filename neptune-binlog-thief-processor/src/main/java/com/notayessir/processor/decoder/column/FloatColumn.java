package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class FloatColumn extends AbsColumn<Float>{

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_FLOAT;
    }

}
