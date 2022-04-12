package com.notayessir.common.column;


/**
 * 表字段信息
 */
public class ColumnDef {

    /**
     * 所处位置
     */
    private int pos;

    /**
     * 是否可以为空
     */
    private boolean nullable;

    /**
     * 字段类型
     */
    private ColumnType columnType;

    /**
     * 元信息
     */
    private byte [] metadata;


    public ColumnDef(int pos, boolean nullable, ColumnType columnType, byte [] metadata) {
        this.pos = pos;
        this.nullable = nullable;
        this.columnType = columnType;
        this.metadata = metadata;
    }

    public byte[] getMetadata() {
        return metadata;
    }

    public void setMetadata(byte[] metadata) {
        this.metadata = metadata;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
