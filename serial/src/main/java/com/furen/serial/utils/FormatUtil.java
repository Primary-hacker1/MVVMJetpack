package com.furen.serial.utils;

import java.math.BigInteger;

/**
 * create by 2022/6/29
 * 串口进制转换类
 *
 * @author zt
 */
public class FormatUtil {
    public static byte[] HexStrToAscii(String InString) {
        int i = 0;
        byte[] by = new byte[InString.length() / 2];
        byte temp = 0;
        int loop = 0;
        // 字符转为byte
        do {
            temp = (byte) Integer.parseInt(InString.substring(i, i + 2), 16);
            //
            by[loop] = temp;
            i = i + 2;
            loop++;
        } while (i < InString.length());
        return by;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * @param hexStr 16进制字符串转二进制
     * @return String
     */
    public static String hexStr2Byte(String hexStr) {
        int i = Integer.parseInt(hexStr, 16);
        return Integer.toBinaryString(i);
    }

    /**
     * @param hexStr 16进制字符串转10进制
     * @return int
     */
    public static int hexStr2Int(String hexStr) {
        BigInteger bigint = new BigInteger(hexStr, 16);
        return bigint.intValue();
    }

    /**
     * 10进制转16进制
     *
     * @param num
     * @return hex
     */
    public static String int2HexStr(Integer num) {
        return Integer.toHexString(num);
    }

    /**
     * 10进制转16进制
     *
     * @param num
     * @return hex  2位
     */
    public static String int2HexStr2(Integer num) {
        return String.format("%02x", num);
    }

    /**
     * @param values 合并多个byte[]为一个byte数组
     * @return byte
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    public static int hexToInt(String str) {
        int a = Integer.parseInt(str, 16);
        if (a < 128) {
            return a;
        } else {
            return (128 - a);
        }
    }

    public static String intToHex(int a) {
        String str = null;
        if (a >= 0) {
            str = Integer.toHexString(a);
        } else {
            str = Integer.toHexString((byte) (128 - a));
        }
        str = "00" + str;
        str = str.substring(str.length() - 2);
        return str;
    }
}
