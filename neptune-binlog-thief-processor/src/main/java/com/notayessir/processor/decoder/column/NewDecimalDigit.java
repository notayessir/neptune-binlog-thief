package com.notayessir.processor.decoder.column;

import java.util.ArrayList;

public class NewDecimalDigit {

    private int occupiedLen;

    private ArrayList<Integer> byteNumbers;

    public NewDecimalDigit(int occupiedLen, ArrayList<Integer> byteNumbers) {
        this.occupiedLen = occupiedLen;
        this.byteNumbers = byteNumbers;
    }

    public ArrayList<Integer> getByteNumbers() {
        return byteNumbers;
    }

    public void setByteNumbers(ArrayList<Integer> byteNumbers) {
        this.byteNumbers = byteNumbers;
    }

    public int getOccupiedLen() {
        return occupiedLen;
    }

    public void setOccupiedLen(int occupiedLen) {
        this.occupiedLen = occupiedLen;
    }

}
