package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class VarStringColumn extends AbsColumn<String>{

    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_VAR_STRING;
    }
}
