package com.notayessir.processor.util;

import com.alibaba.fastjson.JSONObject;
import com.notayessir.processor.database.entity.ColumnDefEntity;
import com.notayessir.processor.decoder.RowSet;
import com.notayessir.processor.decoder.column.Column;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

/**
 * 将对象转换为 json 格式
 */
public class JSONFormatter {



    public static List<JSONFormatData> format(List<ColumnDefEntity> columnDefs, List<RowSet> rowSets){
        List<JSONFormatData> list = new ArrayList<>(rowSets.size());
        for (RowSet rowSet : rowSets) {
            List<Column<?>> newColumns = rowSet.getNewColumns();
            JSONObject newData = formatColumn(columnDefs, newColumns);
            JSONObject oldData = null;
            if (Objects.nonNull(rowSet.getOldColumns())){
                List<Column<?>> oldColumns = rowSet.getOldColumns();
                oldData = formatColumn(columnDefs, oldColumns);
            }
            list.add(new JSONFormatData(newData, oldData));
        }
        return list;
    }

    public static JSONObject formatColumn(List<ColumnDefEntity> columnDefs, List<Column<?>> columns){
        JSONObject data = new JSONObject(columns.size());
        for (Column<?> c : columns) {
            ColumnDefEntity columnDef = columnDefs.get(c.getPos());
            String columnType;
            String[] split;
            // get value of set and enum from column definition,
            // the value of set and enum better do not have any special symbol, like ''
            switch (c.getColumnType()){
                case MYSQL_TYPE_SET:
                    columnType = StringUtils.replace(columnDef.getColumnType(), "set(", "");
                    columnType = StringUtils.removeEnd(columnType, ")");
                    split = columnType.split(",");
                    BitSet bitSet = BitSet.valueOf(new long[]{Long.parseLong(c.getVal().toString())});
//                    System.out.println("bitSet - >" + bitSet);
                    StringBuilder sb = new StringBuilder(64);
                    for (int i = 0; i < split.length; i++){
                        if (bitSet.get(i)){
                            sb.append(StringUtils.replace(split[i], "'", "")).append(",");
                        }
                    }
                    String val = sb.deleteCharAt(sb.length() - 1).toString();
                    data.put(columnDef.getColumnName(), val);
                    break;
                case MYSQL_TYPE_ENUM:
                    columnType = StringUtils.replace(columnDef.getColumnType(), "enum(", "");
                    columnType = StringUtils.removeEnd(columnType, ")");
                    split = columnType.split(",");
                    data.put(columnDef.getColumnName(), StringUtils.replace(split[Integer.parseInt(c.getVal().toString()) - 1], "'", ""));
                    break;
                default:
                    data.put(columnDef.getColumnName(), c.getVal());
            }
        }
        return data;
    }


}
