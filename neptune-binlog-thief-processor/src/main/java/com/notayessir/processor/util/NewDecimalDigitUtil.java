package com.notayessir.processor.util;


import com.notayessir.common.util.ByteUtil;
import com.notayessir.processor.decoder.column.NewDecimalDigit;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

/**
 * 读取 NewDecimal 的工具类
 */
public class NewDecimalDigitUtil {

    /**
     * storage required for remaining digits
     */
    private static final int [][] DECIMAL_STORAGE_TABLE = {{7,9,4}, {5,6,3}, {3,4,2}, {1,2,1}};

    /**
     * parse integer part of decimal
     * @param digits        bytes of integers
     * @param byteNumbers   length of bytes for each integer
     * @return              integer part of decimal
     */
    public static String parseIntegerDigits(byte [] digits, ArrayList<Integer> byteNumbers){
        ArrayUtils.reverse(digits);
        StringBuilder val = new StringBuilder();
        int startIndex = 0;
        for (int n : byteNumbers) {
            int maxLeftoverDigits = DECIMAL_STORAGE_TABLE[Math.abs(n - 4)][1];
            byte[] bytes = ArrayUtils.subarray(digits, startIndex, startIndex + n);
            startIndex += n;
            StringBuilder aInt = new StringBuilder(ByteUtil.readInt(bytes) + "");
            if (aInt.length() != maxLeftoverDigits){
                int paddingLen = maxLeftoverDigits - aInt.length();
                for (int i = 0; i < paddingLen; i++) {
                    aInt.insert(0, "0");
                }
            }
            val.insert(0, aInt);
        }
        return val.toString();
    }

    /**
     * parse fractional part of decimal
     * @param digits        bytes of fractional
     * @param byteNumbers   length of bytes for each integer
     * @param scaleLen      fractional scale
     * @return              fractional part of decimal
     */
    public static String parseFractionalDigits(byte [] digits, ArrayList<Integer> byteNumbers, int scaleLen){
        StringBuilder val = new StringBuilder();
        int startIndex = 0;
        for (int i = byteNumbers.size() - 1; i >= 0; i--) {
            int n = byteNumbers.get(i);
            int maxLeftoverDigits = DECIMAL_STORAGE_TABLE[Math.abs(n - 4)][1];
            byte[] bytes = ArrayUtils.subarray(digits, startIndex, startIndex + n);
            ArrayUtils.reverse(bytes);
            startIndex += n;
            StringBuilder aInt = new StringBuilder(ByteUtil.readInt(bytes) + "");
            if (aInt.length() < maxLeftoverDigits){
                int paddingLen = maxLeftoverDigits - aInt.length();
                if (maxLeftoverDigits + val.length() > scaleLen){
                    paddingLen = scaleLen - (val.length() + aInt.length());
                }
                for (int j = 0; j < paddingLen; j++) {
                    aInt.insert(0, "0");
                }
            }
            val.append(aInt);
        }
        return val.toString();
    }

    /**
     * parse new decimal stored structure from digits
     * @param digits    integer digits or fractional digits
     * @return          new decimal stored structure
     */
    public static NewDecimalDigit parseStruct(int digits){
        int occupiedLen = 0;
        ArrayList<Integer> byteNumbers = new ArrayList<>(8);
        for (int[] ints : DECIMAL_STORAGE_TABLE) {
            int j = digits / ints[1];
            int k = digits % ints[1];
            if (j > 0) {
                occupiedLen += j * ints[2];
                for (int i = 0; i < j; i++){
                    byteNumbers.add(ints[2]);
                }
            }
            if (k >= ints[0]) {
                occupiedLen += ints[2];
                byteNumbers.add(ints[2]);
                break;
            }
            digits = k;
        }
        return new NewDecimalDigit(occupiedLen, byteNumbers);
    }

    /**
     * invert whole bytes
     * @param digits    integer or fractional part of decimal
     */
    public static void invertBytes(byte[] digits) {
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (byte) ~digits[i];
        }
    }

    /**
     * invert highest bit in bytes
     * https://stackoverflow.com/questions/18247126/how-to-flip-a-bit-at-a-specific-position-in-an-integer-in-any-language
     * @param digits    decimal bytes
     */
    public static void invertHighestBit(byte[] digits) {
        byte highestByte = digits[0], highestBitPos = 7;
        highestByte = (byte) (highestByte ^ (1 << highestBitPos));
        digits[0] = highestByte;
    }

    /**
     * check if the highest bit is 1
     * @param highestByte   first byte of decimal bytes
     * @return              highest bit is 1 or not
     */
    public static boolean isPositive(byte highestByte){
        return (highestByte & 128) == 128;
    }
}
