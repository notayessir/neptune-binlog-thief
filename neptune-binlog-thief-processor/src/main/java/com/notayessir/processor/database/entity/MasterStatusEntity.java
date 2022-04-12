package com.notayessir.processor.database.entity;


public class MasterStatusEntity {

    /**
     * 当前 binlog 文件名称
     */
    private String file;

    /**
     * 当前 binlog 位置
     */
    private Long position;


    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
