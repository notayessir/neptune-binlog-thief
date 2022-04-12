package com.notayessir.processor.decoder.column;

import com.notayessir.common.column.ColumnType;

import java.util.Date;

public class DateTimeColumn extends AbsColumn<Date>{

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_DATETIME;
    }

}
