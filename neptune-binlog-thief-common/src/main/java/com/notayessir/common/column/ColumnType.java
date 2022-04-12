package com.notayessir.common.column;

/**
 * 对应 MySQL 字段类型
 */
public enum ColumnType {


    MYSQL_TYPE_DECIMAL(0x00, new String[]{}),
    MYSQL_TYPE_TINY(0x01, new String[]{"tinyint"}),
    MYSQL_TYPE_SHORT(0x02, new String[]{"smallint"}),

    MYSQL_TYPE_LONG(0x03, new String[]{"int"}),

    MYSQL_TYPE_FLOAT(0x04, new String[]{"float"}),
    MYSQL_TYPE_DOUBLE(0x05, new String[]{"double"}),
    MYSQL_TYPE_NULL(0x06, new String[]{}),
    MYSQL_TYPE_TIMESTAMP(0x07, new String[]{}),

    MYSQL_TYPE_LONGLONG(0x08, new String[]{"bigint"}),

    MYSQL_TYPE_INT24(0x09, new String[]{"mediumint"}),

    MYSQL_TYPE_DATE(0x0a, new String[]{"date"}),

    MYSQL_TYPE_TIME(0x0b, new String[]{}),
    MYSQL_TYPE_DATETIME(0x0c, new String[]{}),
    MYSQL_TYPE_YEAR(0x0d, new String[]{"year"}),
    MYSQL_TYPE_NEWDATE(0x0e, new String[]{}),

    MYSQL_TYPE_VARCHAR(0x0f, new String[]{"varchar", "varbinary"}),

    MYSQL_TYPE_BIT(0x10, new String[]{"bit"}),
    MYSQL_TYPE_TIMESTAMP2(0x11, new String[]{"timestamp"}),
    MYSQL_TYPE_DATETIME2(0x12, new String[]{"datetime"}),
    MYSQL_TYPE_TIME2 (0x13, new String[]{"time"}),
    MYSQL_TYPE_JSON(0xf5, new String[]{"json"}),
    MYSQL_TYPE_NEWDECIMAL(0xf6, new String[]{"decimal"}),
    MYSQL_TYPE_ENUM(0xf7, new String[]{"enum"}),
    MYSQL_TYPE_SET(0xf8, new String[]{}),
    MYSQL_TYPE_TINY_BLOB(0xf9, new String[]{}),
    MYSQL_TYPE_MEDIUM_BLOB(0xfa, new String[]{}),
    MYSQL_TYPE_LONG_BLOB(0xfb, new String[]{}),
    MYSQL_TYPE_BLOB(0xfc, new String[]{"tinytext", "tinyblob", "longblob", "longtext", "mediumblob", "mediumtext", "text"}),
    MYSQL_TYPE_VAR_STRING(0xfd, new String[]{}),
    MYSQL_TYPE_STRING(0xfe, new String[]{"set", "enum", "binary", "char"}),
    MYSQL_TYPE_GEOMETRY(0xff, new String[]{"point", "polygon", "geometry", "geomcollection", "linestring", "multilinestring", "multipoint", "multipolygon"}),
    ;


    private final int id;

    /**
     * equal to column DATA_TYPE in 'information_schema'.'COLUMNS'
     */
    private final String [] dataType;


    ColumnType(int id, String [] dataType){
        this.id = id;
        this.dataType = dataType;
    }

    public String [] getDataType() {
        return dataType;
    }

    public int getId() {
        return id;
    }

    /**
     * 根据 id 获取字段类型
     * @param id    id
     * @return      字段类型
     */
    public static ColumnType getByVal(int id){
        ColumnType[] values = ColumnType.values();
        for (ColumnType ct : values) {
            if (ct.getId() == id){
                return ct;
            }
        }
        throw new RuntimeException("no relative column type about " + id);
    }



}

