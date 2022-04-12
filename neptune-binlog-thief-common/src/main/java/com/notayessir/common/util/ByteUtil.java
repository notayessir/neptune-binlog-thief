package com.notayessir.common.util;


import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;

/**
 * packet 工具类，从 byteBuf 中读取 int/byte/string 等
 */
public class ByteUtil {


    /**
     * 从 ByteBuf 中读取以 0 结尾的字符串
     * @param in    ByteBuf
     * @return      字符串
     */
    public static String readStringNull(ByteBuf in){
        in.markReaderIndex();
        int len = 0;
        while (in.readByte() != 0x00){
            len ++;
        }
        in.resetReaderIndex();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        // 00(NUL)
        in.skipBytes(1);
        return new String(bytes);
    }


    /**
     * 从 ByteBuf 读取 int 值
     * @param in    ByteBuf
     * @return      int
     */
    public static int readIntAndRelease(ByteBuf in){
        if (in.readableBytes() > 4){
            throw new RuntimeException("readableBytes can't not exceed 4");
        }
        byte[] bytes = readBytesAndRelease(in);
        return readInt(bytes);
    }

    /**
     * 将数组转为 int 值, bytes 长度不应该大于 4
     * @param bytes     bytes 数组
     * @return          int
     */
    public static int readInt(byte [] bytes){
        int size = bytes.length;
        int val = 0;
        for (int i = size - 1; i >= 0; i--){
            val |= (Byte.toUnsignedInt(bytes[i]) << (i * 8));
        }
        return val;
    }


    /**
     * 按 IEEE 754 规范从 ByteBuf 读取 double
     * @param in    ByteBuf，长度应该等于 8
     * @return      double
     */
    public static double readDoubleAndRelease(ByteBuf in){
        if (in.readableBytes() != 8){
            throw new RuntimeException("readableBytes is not 8");
        }
        byte[] bytes = readBytesAndRelease(in);
        ArrayUtils.reverse(bytes);
        return ByteBuffer.wrap(bytes).getDouble();
    }


    /**
     * 按 IEEE 754 规范从 ByteBuf 读取 float
     * @param in    ByteBuf，长度应该等于 4
     * @return      float
     */
    public static float readFloatAndRelease(ByteBuf in){
        if (in.readableBytes() != 4){
            throw new RuntimeException("readableBytes is not 4");
        }
        byte[] bytes = readBytesAndRelease(in);
        ArrayUtils.reverse(bytes);
        return ByteBuffer.wrap(bytes).getFloat();
    }


    /**
     * 从 ByteBuf 读取 long
     * @param in    ByteBuf 长度应该小于 8
     * @return      long
     */
    public static long readLongAndRelease(ByteBuf in){
        if (in.readableBytes() > 8){
            throw new RuntimeException("readableBytes can't not exceed 8");
        }
        byte[] bytes = readBytesAndRelease(in);
        return readLong(bytes);
    }

    /**
     * 从 bytes 中读取 long
     * @param bytes     bytes，长度小于等于 8
     * @return          long
     */
    public static long readLong(byte [] bytes){
        int size = bytes.length;
        long val = 0;
        for (int i = size - 1; i >= 0; i--){
            val |= (Byte.toUnsignedLong(bytes[i]) << (i * 8));
        }
        return val;
    }


    /**
     * 从 ByteBuf 中读取所有 bytes
     * @param in    ByteBuf
     * @return      bytes
     */
    public static byte[] readBytesAndRelease(ByteBuf in){
        int size = in.readableBytes();
        byte[] bytes = new byte[size];
        in.readBytes(bytes);
        in.release();
        return bytes;
    }

    /**
     * 将 bytes 转为二进制字符串，并填充每个 byte 长度到 8
     * @param bytes    bytes
     * @return         二进制字符串
     */
    public static String bytesToBinaryString(byte [] bytes){
        StringBuilder binary = new StringBuilder(64);
        for (byte b : bytes){
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binary.append(binaryString);
        }
        return binary.toString();
    }

    /**
     * 读取 ByteBuf 并转为字符串
     * @param in    ByteBuf
     * @return      字符串
     */
    public static String readStringAndRelease(ByteBuf in){
        return new String(readBytesAndRelease(in));
    }

    /**
     * 合并高低位功能标识
     * @param lowerCapabilityFlag   低位 2 字节
     * @param upperCapabilityFlag   高位 2 字节
     * @return                      4 字节功能标识
     */
    public static int parseCapabilityFlag(byte [] lowerCapabilityFlag , byte [] upperCapabilityFlag){
        int lower = lowerCapabilityFlag[0] << 8 | lowerCapabilityFlag [1];
        int upper = upperCapabilityFlag[0] << 8 | upperCapabilityFlag [1];
        return upper << 16 | lower;
    }

    /**
     * 解析状态标志
     * @param statusFlags   服务器状态 bytes
     * @return              int
     */
    public static int parseStatusFlag(byte [] statusFlags){
        return statusFlags[0] << 8 | statusFlags [1];
    }


    /**
     * 将 int 转为 bytes 数组，least significant byte
     * @param aInt  int
     * @return      least significant byte arr
     */
    public static byte[] intToBytes(int aInt) {
        return new byte[] {
                (byte)((aInt) & 0xff),
                (byte)((aInt >> 8) & 0xff),
                (byte)((aInt >> 16) & 0xff),
                (byte)((aInt >> 24) & 0xff)
        };
    }

    /**
     * 将 int 转换成 3 byte 数组
     * @param payloadLen    payload 长度
     * @return              byte 数组
     */
    public static byte [] readPayloadLen(int payloadLen){
        byte [] aInt = intToBytes(payloadLen);
        byte [] bytes = new byte[3];
        System.arraycopy(aInt, 0, bytes, 0, 3);
        return bytes;
    }

    /**
     * 读取 Length-Encoded Integer Type
     * @param in    输入流
     * @return      1, 3, 4, or 9 bytes, least significant byte first.
     */
    public static int readEncodedInt(ByteBuf in){
        int index = in.readerIndex();
        int encodedVal = Byte.toUnsignedInt(in.readByte());
        if (encodedVal < 251){
            return encodedVal;
        }
        in.readerIndex(index);
        if (encodedVal == 0xfc){
            return readIntAndRelease(in.readBytes(2));
        }
        if (encodedVal == 0xfd){
            return readIntAndRelease(in.readBytes(3));
        }
        // 0xfe
        return readIntAndRelease(in.readBytes(8));
    }


}
