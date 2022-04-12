package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

import java.time.LocalTime;

public class Time2Column extends AbsColumn<LocalTime> {


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_TIME2;
    }

}
