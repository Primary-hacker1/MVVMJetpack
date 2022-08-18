package com.just.machine

import android.app.Activity
import android.app.Application
import com.common.log.ThinkerLogger
import com.just.machine.helper.UriConfig
import com.just.machine.model.SharedPreferencesUtils
import com.justsafe.libview.util.SystemUtil


import dagger.hilt.android.HiltAndroidApp
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.AutoSizeLog
import java.util.*

@HiltAndroidApp
open class App : Application() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        ThinkerLogger.getInstance().init(UriConfig.LOG_PATH)//日志输出本地

        AutoSize.initCompatMultiProcess(this)//今日头条终极适配
        AutoSizeConfig.getInstance() //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            .setUseDeviceSize(true)
            .setBaseOnWidth(false)//全局适配宽高
            //如果没有这个需求建议不开启
            .setCustomFragment(true).onAdaptListener = object : onAdaptListener {
            override fun onAdaptBefore(target: Any, activity: Activity) {
                AutoSizeLog.d(
                    String.format(
                        Locale.ENGLISH,
                        "%s onAdaptBefore!",
                        target.javaClass.name
                    )
                )
            }

            override fun onAdaptAfter(target: Any, activity: Activity) {
                AutoSizeLog.d(
                    String.format(
                        Locale.ENGLISH,
                        "%s onAdaptAfter!",
                        target.javaClass.name
                    )
                )
            }
        }
    }
}