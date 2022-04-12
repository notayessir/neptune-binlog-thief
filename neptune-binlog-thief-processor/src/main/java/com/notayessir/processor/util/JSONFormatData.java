package com.notayessir.processor.util;

import com.alibaba.fastjson.JSONObject;

/**
 * json 格式的 binlog 变更数据
 */
public class JSONFormatData {

    /**
     * 新数据
     */
    private JSONObject data;

    /**
     * 旧数据
     */
    private JSONObject oldData;

    public JSONFormatData(JSONObject data, JSONObject oldData) {
        this.data = data;
        this.oldData = oldData;
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
