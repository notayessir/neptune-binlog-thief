package com.notayessir.processor.database.repository;

import com.notayessir.processor.database.entity.ColumnDefEntity;
import com.notayessir.processor.database.entity.MasterStatusEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends CrudRepository<ColumnDefEntity, Long> {

    /**
     *  根据数据库、表名查询表结构
     * @param schemaName    数据库
     * @param tableName     表名
     * @return              表结构
     */
    @Query("SELECT * FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = :schemaName AND TABLE_NAME = :tableName ORDER BY ORDINAL_POSITION")
    List<ColumnDefEntity> find(@Param("schemaName") String schemaName, @Param("tableName") String tableName);


    /**
     * 查询当前 binlog 位置
     * @return  binlog 位置
     */
    @Query("show master status")
    MasterStatusEntity showMasterStatus();

}
