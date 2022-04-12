package com.notayessir.common.util;


import com.notayessir.common.column.ColumnType;
import io.netty.buffer.ByteBuf;

/**
 * MySQL metadata 工具类
 * https://dev.mysql.com/doc/dev/mysql-server/latest/classbinary__log_1_1Table__map__event.html#ae6f07aa4e4914353bc90f10e743eaf81
 */

public class MetadataUtil {

    /**
     * 根据字段类型，从 ByteBuf 中读取元信息字节
     * @param columnType    字段类型
     * @param in            ByteBuf
     * @return              占用的元信息字节
     */
    public static byte [] readMetadata(ColumnType columnType, ByteBuf in){
        switch (columnType){
            case MYSQL_TYPE_DECIMAL:
            case MYSQL_TYPE_TINY:
            case MYSQL_TYPE_SHORT:
            case MYSQL_TYPE_LONG:
            case MYSQL_TYPE_NULL:
            case MYSQL_TYPE_TIMESTAMP:
            case MYSQL_TYPE_LONGLONG:
            case MYSQL_TYPE_INT24:
            case MYSQL_TYPE_DATE:
            case MYSQL_TYPE_TIME:
            case MYSQL_TYPE_DATETIME:
            case MYSQL_TYPE_YEAR:
            case MYSQL_TYPE_NEWDATE:
            case MYSQL_TYPE_ENUM:
            case MYSQL_TYPE_SET:
            case MYSQL_TYPE_TINY_BLOB:
            case MYSQL_TYPE_MEDIUM_BLOB:
            case MYSQL_TYPE_LONG_BLOB:
                return new byte[0];
            case MYSQL_TYPE_FLOAT:
            case MYSQL_TYPE_DOUBLE:
            case MYSQL_TYPE_BLOB:
            case MYSQL_TYPE_GEOMETRY:

            case MYSQL_TYPE_TIME2:
            case MYSQL_TYPE_DATETIME2:
            case MYSQL_TYPE_TIMESTAMP2:
                return ByteUtil.readBytesAndRelease(in.readBytes(1));
            case MYSQL_TYPE_VARCHAR:
            case MYSQL_TYPE_BIT:
            case MYSQL_TYPE_NEWDECIMAL:
            case MYSQL_TYPE_VAR_STRING:
            case MYSQL_TYPE_STRING:
                return ByteUtil.readBytesAndRelease(in.readBytes(2));
        }
        return new byte[0];
    }


}
