package com.notayessir.processor.disruptor;


import com.alibaba.fastjson.JSONObject;
import com.notayessir.common.packet.EventHeader;
import com.notayessir.common.packet.PacketHeader;
import com.notayessir.common.packet.PacketType;
import com.notayessir.common.packet.event.TableMap;

/**
 * binlog 事件信息
 */
public class BinlogEvent {

    private Long xid;

    /**
     * 所处数据库
     */
    private String database;

    /**
     * 变更的 sql
     */
    private String query;

    /**
     * 帧类型
     */
    private PacketType packetType;

    /**
     * 表信息
     */
    private TableMap tableMap;

    /**
     * 插入/修改的新数据
     */
    private JSONObject data;

    /**
     * 更新的旧数据
     */
    private JSONObject oldData;

    /**
     * 帧头部
     */
    private PacketHeader packetHeader;

    /**
     * binlog 事件头部
     */
    private EventHeader eventHeader;

    public BinlogEvent() {
    }

    public BinlogEvent(PacketType packetType, TableMap tableMap, JSONObject data, JSONObject oldData) {
        this.packetType = packetType;
        this.tableMap = tableMap;
        this.data = data;
        this.oldData = oldData;
    }

    public Long getXid() {
        return xid;
    }

    public void setXid(Long xid) {
        this.xid = xid;
    }

    public PacketHeader getPacketHeader() {
        return packetHeader;
    }

    public void setPacketHeader(PacketHeader packetHeader) {
        this.packetHeader = packetHeader;
    }

    public EventHeader getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(EventHeader eventHeader) {
        this.eventHeader = eventHeader;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

    public TableMap getTableMap() {
        return tableMap;
    }

    public void setTableMap(TableMap tableMap) {
        this.tableMap = tableMap;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getOldData() {
        return oldData;
    }

    public void setOldData(JSONObject oldData) {
        this.oldData = oldData;
    }

}
