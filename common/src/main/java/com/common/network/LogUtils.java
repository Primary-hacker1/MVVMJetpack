package com.common.network;

import android.util.Log;

import androidx.viewbinding.BuildConfig;


public class LogUtils {
	public static boolean isDebug = BuildConfig.DEBUG;
	public static <T> void d(String tag, T msg) {
		if (isDebug) {
			Log.d(tag, String.valueOf(msg));
		}
	}
	public static <T> void e(String tag, T msg) {
		if (isDebug) {
			Log.e(tag, String.valueOf(msg));
		}
	}
	public static <T> void e(String tag, T msg, Throwable tr){
		if(isDebug){
			Log.e(tag, String.valueOf(msg), tr);
		}
	}
	public static <T> void i(String tag, T msg) {
		if (isDebug) {

			Log.i(tag, String.valueOf(msg));
		}
	}
	public static void printJson(String tag, String msg){
		if (isDebug) {

			if (tag == null || tag.length() == 0 || msg == null || ((String) msg).length() == 0) {
				return;
			}
			int segmentSize = 3 * 1024;
			long length = ((String) msg).length();
			if (length <= segmentSize) {
				// 长度小于等于限制直接打印
				Log.i(tag, ((String) msg));
			} else {
				while (((String) msg).length() > segmentSize) {// 循环分段打印日志
					String logContent = ((String) msg).substring(0, segmentSize);
					msg = ((String) msg).replace(logContent, "");
					Log.i("", logContent);
				}
				Log.i("", ((String) msg));
				// 打印剩余日志 }
			}
		}

	}
	public static <T> void v(String tag, T msg) {
		if (isDebug) {
			Log.v(tag, String.valueOf(msg));
		}
	}

	public static <T> void w(String tag, T msg) {
		if (isDebug) {
			Log.w(tag, String.valueOf(msg));
		}
	}




}