package com.notayessir.common.util;

import java.util.zip.CRC32;

/**
 * crc32 工具类
 */
public class CRC32Util {


    /**
     * 对 bytes 进行 crc32 运算
     * @param bytes 字节流
     * @return      crc32 值
     */
    public static long CRC32(byte [] bytes){
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }

}
