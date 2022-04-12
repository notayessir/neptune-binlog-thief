package com.notayessir.common;


/**
 * binlog 位置
 */
public class BinlogPosition {


    /**
     * 所处 binlog file
     */
    private String file;

    /**
     * 所处 binlog position
     */
    private Long pos;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "BinlogPosition{" +
                "file='" + file + '\'' +
                ", pos=" + pos +
                '}';
    }
}
