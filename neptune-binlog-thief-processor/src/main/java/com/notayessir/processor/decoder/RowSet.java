package com.notayessir.processor.decoder;

import com.notayessir.processor.decoder.column.Column;

import java.util.List;

public class RowSet {


    private List<Column<?>> newColumns;

    private List<Column<?>> oldColumns;

    public RowSet(List<Column<?>> newColumns, List<Column<?>> oldColumns) {
        this.newColumns = newColumns;
        this.oldColumns = oldColumns;
    }

    public List<Column<?>> getNewColumns() {
        return newColumns;
    }

    public void setNewColumns(List<Column<?>> newColumns) {
        this.newColumns = newColumns;
    }

    public List<Column<?>> getOldColumns() {
        return oldColumns;
    }

    public void setOldColumns(List<Column<?>> oldColumns) {
        this.oldColumns = oldColumns;
    }

}
