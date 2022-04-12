package com.notayessir.processor.util;

/**
 * 精度处理工具类
 */
public class FSPUtil {


    public static int getLen(int precision){
        if (precision == 1 || precision == 2){
            return 1;
        }
        if (precision == 3 || precision == 4){
            return 2;
        }
        if (precision == 5 || precision == 6){
            return 3;
        }
        return 0;
    }


}
