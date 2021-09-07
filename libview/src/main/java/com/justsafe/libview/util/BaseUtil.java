package com.justsafe.libview.util;


public class BaseUtil {

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {//快速双击创建多个bug
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
