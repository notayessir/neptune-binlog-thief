package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class LongColumn extends AbsColumn<Long> {


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_LONG;
    }

}
