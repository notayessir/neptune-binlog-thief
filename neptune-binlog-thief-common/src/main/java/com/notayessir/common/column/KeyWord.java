package com.notayessir.common.column;

/**
 * MySQL 关键字
 */
public enum KeyWord {


    COMMIT("commit"),

    BEGIN("begin"),

    ADD_COLUMN("add column"),

    DROP_COLUMN("drop column");


    private final String val;

    KeyWord(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
