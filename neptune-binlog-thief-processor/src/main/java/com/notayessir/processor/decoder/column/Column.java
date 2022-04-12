package com.notayessir.processor.decoder.column;


import com.notayessir.common.column.ColumnType;

public interface Column<C> {

    C getVal();

    void setVal(C val);

    ColumnType getColumnType();

    int getPos();

    void setPos(int pos);

}
