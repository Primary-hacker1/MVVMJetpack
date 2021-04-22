package com.justsafe.libview.invite;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justsafe.libview.invite.bean.PersonInfor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * SharePreferences操作工具类
 */
public class SpUtil {
    private static String tag = SpUtil.class.getSimpleName();
    private final static String SP_NAME = "neeting_room";
    private static SharedPreferences sp;

    /**
     * 保存布尔值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存字符串
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putString(key, value).commit();

    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().clear().commit();
    }

    /**
     * 保存long型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, long value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putLong(key, value).commit();
    }

    /**
     * 保存int型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 保存float型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        sp.edit().putFloat(key, value).commit();
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 获取int值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getInt(key, defValue);
    }

    /**
     * 获取long值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(Context context, String key, float defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp.getBoolean(key, defValue);
    }


    public static void saveList(Context ctx, String key,List<Object> list) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }

    public static List<Object> getList(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString(key, null);
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        List<Object> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    /**
     * 存储当前用户
     */
    public static void savePerson(Context ctx, String key,List<PersonInfor> List) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(List);
        editor.putString(key, json);
        editor.commit();
    }

    /**
     * 读取当前用户信息
     */
    public static List<PersonInfor> getPerson(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString(key, null);
        Type type = new TypeToken<List<PersonInfor>>() {
        }.getType();
        List<PersonInfor> arrayList = gson.fromJson(json, type);
        return arrayList;

    }
}


