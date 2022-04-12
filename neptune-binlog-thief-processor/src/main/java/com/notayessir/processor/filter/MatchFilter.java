package com.notayessir.processor.filter;

import com.notayessir.common.column.ColumnDef;
import com.notayessir.common.packet.event.TableMap;
import com.notayessir.processor.database.entity.ColumnDefEntity;
import com.notayessir.processor.database.manager.ColumnManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 表字段匹配过滤器，将 binlog 事件的字段信息与当前数据库字段信息对比，若有变化，则跳过生产
 */
@Component
public class MatchFilter {


    private static final Logger LOG = LoggerFactory.getLogger(MatchFilter.class);



    @Autowired
    ColumnManager columnManager;

    public boolean filter(TableMap tableMap) {
        String dbName = tableMap.getSchemaName();
        String tbName = tableMap.getTableName();
        List<ColumnDefEntity> columnEntities = columnManager.find(dbName, tbName);

        // check the size of field
        List<ColumnDef> columnDefs = tableMap.getColumnDefs();
        if (columnEntities.size() != columnDefs.size()){
            LOG.warn("size of column is not equal, this event is ignored. current size: {}, table map size: {}", columnEntities.size(), columnDefs.size());
            return true;
        }

        // check the definition of field
        for (int i = 0; i < columnDefs.size(); i++) {
            ColumnDef c1 = columnDefs.get(i);
            ColumnDefEntity c2 = columnEntities.get(i);
            String[] dataTypes = c1.getColumnType().getDataType();
            boolean equal = false;
            for (String dataType : dataTypes) {
                if (StringUtils.equalsIgnoreCase(dataType, c2.getDataType())){
                    equal = true;
                }
            }
            if (!equal){
                LOG.warn("type of column is not equal, this event is ignored. current type: {}, table map type: {}", c2.getDataType(), Arrays.toString(dataTypes));
                return true;
            }
        }

        return false;
    }

}
