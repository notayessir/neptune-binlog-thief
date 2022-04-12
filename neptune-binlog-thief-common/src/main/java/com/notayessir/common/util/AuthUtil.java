package com.notayessir.common.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * MySQL 密码加密工具
 */
public class AuthUtil {


    /**
     * 使用 old password 对密码进行加密
     * @param password  密码
     * @return          加密后的密码
     */
    public static String oldPassword(byte[] password) {
        int[] result = new int[2];
        int nr = 1345345333;
        int add = 7;
        int nr2 = 0x12345671;
        int tmp;

        int i;
        for (i = 0; i < password.length; i++) {
            if (password[i] == ' ' || password[i] == '\t')
                continue;

            tmp = password[i];
            nr ^= (((nr & 63) + add) * tmp) + (nr << 8);
            nr2 += (nr2 << 8) ^ nr;
            add += tmp;
        }

        result[0] = nr & ((1 << 31) - 1);
        int val = ((1 << 31) - 1);
        result[1] = nr2 & val;
        String hash = String.format("%08x%08x",result[0],result[1]);
        return hash.toLowerCase();
    }


    /**
     * 使用 native password 对密码进行加密
     * @param password      密码
     * @param randomData    随机数据
     * @return              加密后的密码
     */
    public static byte [] nativePassword(byte[] password, byte[] randomData){
        byte[] sha1Pass = DigestUtils.sha1(password);
        byte[] doubleSha1Pass = DigestUtils.sha1(sha1Pass);
        byte [] bytes = new byte[randomData.length + doubleSha1Pass.length];
        System.arraycopy(randomData, 0, bytes, 0, randomData.length);
        System.arraycopy(doubleSha1Pass, 0, bytes, randomData.length, doubleSha1Pass.length);
        return xorWithKey(sha1Pass, DigestUtils.sha1(bytes));
    }

    /**
     * 按位异或，a 与 b 的长度应该相等
     * @param a     bytes a
     * @param b     bytes b
     * @return      异或后的 bytes
     */
    private static byte[] xorWithKey(byte[] a, byte[] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ b[i%b.length]);
        }
        return out;
    }



}
