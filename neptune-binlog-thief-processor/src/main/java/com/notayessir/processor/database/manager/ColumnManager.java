package com.notayessir.processor.database.manager;


import com.notayessir.processor.database.entity.ColumnDefEntity;
import com.notayessir.processor.database.repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColumnManager {


    @Autowired
    ColumnRepository columnRepository;

    @Cacheable(cacheNames = "columns")
    public List<ColumnDefEntity> find(String schema, String table){
        return columnRepository.find(schema, table);
    }


    @CacheEvict(cacheNames = "columns")
    public void clear(String schema, String table){}

}
