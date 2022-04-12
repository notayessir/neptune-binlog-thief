package com.notayessir.common.packet.event;

import com.notayessir.common.column.ColumnDef;

import java.util.List;

public class TableMap {

    private String schemaName;

    private String tableName;

    private Long tableId;

    private List<ColumnDef> columnDefs;


    public TableMap(String schemaName, String tableName, Long tableId, List<ColumnDef> columnDefs) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.tableId = tableId;
        this.columnDefs = columnDefs;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<ColumnDef> getColumnDefs() {
        return columnDefs;
    }

    public void setColumnDefs(List<ColumnDef> columnDefs) {
        this.columnDefs = columnDefs;
    }
}
