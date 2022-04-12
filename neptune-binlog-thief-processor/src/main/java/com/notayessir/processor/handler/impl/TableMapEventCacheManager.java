package com.notayessir.processor.handler.impl;


import com.notayessir.common.packet.event.TableMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TableMapEventCacheManager {


    private final static Map<Long, TableMap> map = new HashMap<>();

    public void put(TableMap tableDef) {
        map.put(tableDef.getTableId(), tableDef);
    }

    public boolean contain(Long tableId){
        return map.containsKey(tableId);
    }

    public void clear(){
        map.clear();
    }

    public TableMap get(Long tableId){
        return map.get(tableId);
    }


}
