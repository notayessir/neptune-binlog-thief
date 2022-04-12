package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class LongLongColumn extends AbsColumn<Long>{


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_LONGLONG;
    }

}
