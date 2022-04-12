package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public class MediumBlobColumn extends AbsColumn<byte[]> {


    @Override
    public ColumnType getColumnType() {
        return ColumnType.MYSQL_TYPE_MEDIUM_BLOB;
    }

}
