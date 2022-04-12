package com.notayessir.processor.decoder.column;

public abstract class AbsColumn<C> implements Column<C> {

    /**
     * value of column
     */
    protected C val;

    /**
     * position in the table
     */
    protected int pos;

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public C getVal() {
        return val;
    }

    @Override
    public void setVal(C val) {
        this.val = val;
    }
}
