package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class DecimalColumn extends AbsColumn<String> {

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_DECIMAL;
    }

}
