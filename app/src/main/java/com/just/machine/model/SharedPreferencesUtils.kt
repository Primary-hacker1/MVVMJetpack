package com.just.machine.model

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.just.machine.App


/**
 *create by 2022/6/30
 *@author zt
 */
class SharedPreferencesUtils private constructor() {

    companion object {
        val instance = SharedPreferencesUtils()
        private const val PER_USERNAME = "per_username"
        private const val NAME = "per_user_name"
    }

    var serialNo: String? = null
        get() {
            if (field == null) {
                field = CommonSharedPreferences.getSPValue(NAME, "")
            }
            return field
        }
        set(serialNo) {
            field = serialNo
            CommonSharedPreferences.setSPValue(NAME, serialNo)
        }

    /**
     * 登录信息销毁
     */
    fun logout() {
        serialNo = ""
    }

}

object CommonSharedPreferences {

    /**
     * Created by lollipop on 2017/12/10.
     * Update by lollipop on 2020/11/09
     * @author Lollipop
     * 持久化储存的工具类
     */

    private const val USER = "Settings"

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    operator fun <T> Context.set(key: String, value: T) {
        val mShareConfig = applicationContext.getSharedPreferences(USER, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> setSPValue(key: String, value: T) {
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Activity中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> Activity.privateSet(key: String, value: T) {
        val name = this.javaClass.simpleName
        val mShareConfig = getSharedPreferences(name, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Fragment中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    fun <T> Fragment.privateSet(key: String, value: T) {
        val name = this.javaClass.simpleName
        val mShareConfig = context!!.getSharedPreferences(name, Context.MODE_PRIVATE)
        put(mShareConfig, key, value)
    }

    /**
     * 储存数据到一个指定的SharedPreferences中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    private fun <T> put(mShareConfig: SharedPreferences, key: String, value: T) {
        val conEdit = mShareConfig.edit()
        when (value) {
            is String -> conEdit.putString(key, (value as String).trim { it <= ' ' })
            is Long -> conEdit.putLong(key, value as Long)
            is Boolean -> conEdit.putBoolean(key, value as Boolean)
            is Int -> conEdit.putInt(key, value as Int)
            is Float -> conEdit.putFloat(key, value as Float)
        }
        conEdit.apply()
    }

    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    operator fun <T> Context.get(key: String, defValue: T): T {
        val mShareConfig = applicationContext.getSharedPreferences(USER, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> getSPValue(key: String, defValue: T): T {
        val mShareConfig = App.instance!!.getSharedPreferences(USER, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Activity中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> Activity.privateGet(key: String, defValue: T): T {
        val name = this.javaClass.simpleName
        val mShareConfig = getSharedPreferences(name, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从Fragment中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    fun <T> Fragment.privateGet(key: String, defValue: T): T {
        val name = this.javaClass.simpleName
        val mShareConfig = context!!.getSharedPreferences(name, Context.MODE_PRIVATE)
        return get(mShareConfig, key, defValue)
    }

    /**
     * 从指定的SharedPreferences中获取数据
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(mShareConfig: SharedPreferences, key: String, defValue: T): T {
        var value: T = defValue
        when (defValue) {
            is String -> value = mShareConfig.getString(key, defValue as String) as T
            is Long -> value =
                java.lang.Long.valueOf(mShareConfig.getLong(key, defValue as Long)) as T
            is Boolean -> value =
                java.lang.Boolean.valueOf(mShareConfig.getBoolean(key, defValue as Boolean)) as T
            is Int -> value = Integer.valueOf(mShareConfig.getInt(key, defValue as Int)) as T
            is Float -> value =
                java.lang.Float.valueOf(mShareConfig.getFloat(key, defValue as Float)) as T
        }
        return value
    }
}