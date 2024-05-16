/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.autosize

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.util.SparseArray
import me.jessyan.autosize.external.ExternalAdaptInfo
import me.jessyan.autosize.external.ExternalAdaptManager
import me.jessyan.autosize.internal.CancelAdapt
import me.jessyan.autosize.internal.CustomAdapt
import me.jessyan.autosize.unit.Subunits
import me.jessyan.autosize.utils.AutoSizeLog
import me.jessyan.autosize.utils.Preconditions
import java.util.Locale

/**
 * ================================================
 * AndroidAutoSize 用于屏幕适配的核心方法都在这里, 核心原理来自于 [今日头条官方适配方案](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
 * 此方案只要应用到 [Activity] 上, 这个 [Activity] 下的所有 Fragment、[Dialog]、
 * 自定义 [View] 都会达到适配的效果, 如果某个页面不想使用适配请让该 [Activity] 实现 [CancelAdapt]
 *
 *
 * 任何方案都不可能完美, 在成本和收益中做出取舍, 选择出最适合自己的方案即可, 在没有更好的方案出来之前, 只有继续忍耐它的不完美, 或者自己作出改变
 * 既然选择, 就不要抱怨, 感谢 今日头条技术团队 和 张鸿洋 等人对 Android 屏幕适配领域的的贡献
 *
 *
 * Created by JessYan on 2018/8/8 19:20
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class AutoSize private constructor() {
    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {
        private val mCache = SparseArray<DisplayMetricsInfo>()
        private const val MODE_SHIFT = 30
        private const val MODE_MASK = 0x3 shl MODE_SHIFT
        private const val MODE_ON_WIDTH = 1 shl MODE_SHIFT
        private const val MODE_DEVICE_SIZE = 2 shl MODE_SHIFT

        /**
         * 检查 AndroidAutoSize 是否已经初始化
         *
         * @return `false` 表示 AndroidAutoSize 还未初始化, `true` 表示 AndroidAutoSize 已经初始化
         */
        fun checkInit(): Boolean {
            return AutoSizeConfig.instance?.initDensity != -1f
        }

        /**
         * 由于 AndroidAutoSize 会通过 [InitProvider] 的实例化而自动完成初始化, 并且 [AutoSizeConfig.init]
         * 只允许被调用一次, 否则会报错, 所以 [AutoSizeConfig.init] 的调用权限并没有设为 public, 不允许外部使用者调用
         * 但由于某些 issues 反应, 可能会在某些特殊情况下出现 [InitProvider] 未能正常实例化的情况, 导致 AndroidAutoSize 未能完成初始化
         * 所以提供此静态方法用于让外部使用者在异常情况下也可以初始化 AndroidAutoSize, 在 [Application.onCreate] 中调用即可
         *
         * @param application [Application]
         */
        fun checkAndInit(application: Application?) {
            if (!checkInit()) {
                AutoSizeConfig.instance
                    ?.setLog(true)
                    ?.init(application)
                    ?.setUseDeviceSize(false)
            }
        }

        /**
         * 使用 AndroidAutoSize 初始化时设置的默认适配参数进行适配 (AndroidManifest 的 Meta 属性)
         *
         * @param activity [Activity]
         */
        fun autoConvertDensityOfGlobal(activity: Activity?) {
            if (AutoSizeConfig.instance?.isBaseOnWidth == true) {
                AutoSizeConfig.instance?.designWidthInDp?.toFloat()?.let {
                    autoConvertDensityBaseOnWidth(
                        activity,
                        it
                    )
                }
            } else {
                AutoSizeConfig.instance?.designHeightInDp?.toFloat()?.let {
                    autoConvertDensityBaseOnHeight(
                        activity,
                        it
                    )
                }
            }
        }

        /**
         * 使用 [Activity] 或 Fragment 的自定义参数进行适配
         *
         * @param activity    [Activity]
         * @param customAdapt [Activity] 或 Fragment 需实现 [CustomAdapt]
         */
        fun autoConvertDensityOfCustomAdapt(activity: Activity?, customAdapt: CustomAdapt) {
            Preconditions.checkNotNull(customAdapt, "customAdapt == null")
            var sizeInDp = customAdapt.sizeInDp

            //如果 CustomAdapt#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
            if (sizeInDp <= 0) {
                sizeInDp = if (customAdapt.isBaseOnWidth) {
                    AutoSizeConfig.instance!!.designWidthInDp.toFloat()
                } else {
                    AutoSizeConfig.instance?.designHeightInDp?.toFloat()
                } ?: 0f // 添加一个默认值，以处理 AutoSizeConfig.instance 可能为 null 的情况
            }

            autoConvertDensity(activity, sizeInDp, customAdapt.isBaseOnWidth)
        }

        /**
         * 使用外部三方库的 [Activity] 或 Fragment 的自定义适配参数进行适配
         *
         * @param activity          [Activity]
         * @param externalAdaptInfo 三方库的 [Activity] 或 Fragment 提供的适配参数, 需要配合 [ExternalAdaptManager.addExternalAdaptInfoOfActivity]
         */
        fun autoConvertDensityOfExternalAdaptInfo(
            activity: Activity?,
            externalAdaptInfo: ExternalAdaptInfo
        ) {
            Preconditions.checkNotNull(externalAdaptInfo, "externalAdaptInfo == null")
            var sizeInDp = externalAdaptInfo.sizeInDp

            // 如果 ExternalAdaptInfo#getSizeInDp() 返回 0, 则使用在 AndroidManifest 上填写的设计图尺寸
            if (sizeInDp <= 0) {
                sizeInDp = if (externalAdaptInfo.isBaseOnWidth) {
                    AutoSizeConfig.instance?.designWidthInDp?.toFloat()
                } else {
                    AutoSizeConfig.instance?.designHeightInDp?.toFloat()
                } ?: 0f // 添加一个默认值，以处理 AutoSizeConfig.instance 可能为 null 的情况
            }
            autoConvertDensity(activity, sizeInDp, externalAdaptInfo.isBaseOnWidth)
        }


        /**
         * 以宽度为基准进行适配
         *
         * @param activity        [Activity]
         * @param designWidthInDp 设计图的总宽度
         */
        fun autoConvertDensityBaseOnWidth(activity: Activity?, designWidthInDp: Float) {
            autoConvertDensity(activity, designWidthInDp, true)
        }

        /**
         * 以高度为基准进行适配
         *
         * @param activity         [Activity]
         * @param designHeightInDp 设计图的总高度
         */
        fun autoConvertDensityBaseOnHeight(activity: Activity?, designHeightInDp: Float) {
            autoConvertDensity(activity, designHeightInDp, false)
        }

        /**
         * 这里是今日头条适配方案的核心代码, 核心在于根据当前设备的实际情况做自动计算并转换 [DisplayMetrics.density]、
         * [DisplayMetrics.scaledDensity]、[DisplayMetrics.densityDpi] 这三个值, 额外增加 [DisplayMetrics.xdpi]
         * 以支持单位 `pt`、`in`、`mm`
         *
         * @param activity      [Activity]
         * @param sizeInDp      设计图上的设计尺寸, 单位 dp, 如果 {@param isBaseOnWidth} 设置为 `true`,
         * {@param sizeInDp} 则应该填写设计图的总宽度, 如果 {@param isBaseOnWidth} 设置为 `false`,
         * {@param sizeInDp} 则应该填写设计图的总高度
         * @param isBaseOnWidth 是否按照宽度进行等比例适配, `true` 为以宽度进行等比例适配, `false` 为以高度进行等比例适配
         * @see [今日头条官方适配方案](https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA)
         */
        fun autoConvertDensity(activity: Activity?, sizeInDp: Float, isBaseOnWidth: Boolean) {
            Preconditions.checkNotNull(activity, "activity == null")
            Preconditions.checkMainThread()

            var subunitsDesignSize: Float =
                if (isBaseOnWidth) AutoSizeConfig.instance!!.unitsManager.designWidth
                else AutoSizeConfig.instance!!.unitsManager.designHeight
            subunitsDesignSize = if (subunitsDesignSize > 0) subunitsDesignSize else sizeInDp

            val screenSize: Int =
                if (isBaseOnWidth) AutoSizeConfig.instance!!.screenWidth
                else AutoSizeConfig.instance!!.screenHeight

            var key = (Math.round(
                (sizeInDp + subunitsDesignSize + screenSize) * AutoSizeConfig.instance!!
                    .initScaledDensity
            ) and MODE_MASK.inv().toLong().toInt())
            key = if (isBaseOnWidth) (key or MODE_ON_WIDTH) else (key and MODE_ON_WIDTH.inv())
            key = if (AutoSizeConfig.instance!!.isUseDeviceSize
            ) (key or MODE_DEVICE_SIZE) else (key and MODE_DEVICE_SIZE.inv())

            val displayMetricsInfo = mCache[key]

            var targetDensity = 0f
            var targetDensityDpi = 0
            var targetScaledDensity = 0f
            var targetXdpi = 0f
            val targetScreenWidthDp: Int
            val targetScreenHeightDp: Int

            if (displayMetricsInfo == null) {
                targetDensity = if (isBaseOnWidth) {
                    AutoSizeConfig.instance!!.screenWidth * 1.0f / sizeInDp
                } else {
                    AutoSizeConfig.instance!!.screenHeight * 1.0f / sizeInDp
                }
                if (AutoSizeConfig.instance!!.privateFontScale > 0) {
                    targetScaledDensity =
                        targetDensity * AutoSizeConfig.instance!!.privateFontScale
                } else {
                    val systemFontScale = if (AutoSizeConfig.instance!!.isExcludeFontScale) 1f else AutoSizeConfig.instance!!.initScaledDensity * 1.0f / AutoSizeConfig.instance!!.initDensity
                    targetScaledDensity = targetDensity * systemFontScale
                }
                targetDensityDpi = (targetDensity * 160).toInt()

                targetScreenWidthDp =
                    (AutoSizeConfig.instance!!.screenWidth / targetDensity).toInt()
                targetScreenHeightDp =
                    (AutoSizeConfig.instance!!.screenHeight / targetDensity).toInt()

                targetXdpi = if (isBaseOnWidth) {
                    AutoSizeConfig.instance!!.screenWidth * 1.0f / subunitsDesignSize
                } else {
                    AutoSizeConfig.instance!!.screenHeight * 1.0f / subunitsDesignSize
                }

                mCache.put(
                    key,
                    DisplayMetricsInfo(
                        targetDensity,
                        targetDensityDpi,
                        targetScaledDensity,
                        targetXdpi,
                        targetScreenWidthDp,
                        targetScreenHeightDp
                    )
                )
            } else {
                targetDensity = displayMetricsInfo.density
                targetDensityDpi = displayMetricsInfo.densityDpi
                targetScaledDensity = displayMetricsInfo.scaledDensity
                targetXdpi = displayMetricsInfo.xdpi
                targetScreenWidthDp = displayMetricsInfo.screenWidthDp
                targetScreenHeightDp = displayMetricsInfo.screenHeightDp
            }

            setDensity(activity, targetDensity, targetDensityDpi, targetScaledDensity, targetXdpi)
            setScreenSizeDp(activity, targetScreenWidthDp, targetScreenHeightDp)

            AutoSizeLog.d(
                String.format(
                    Locale.ENGLISH,
                    "The %s has been adapted! \n%s Info: isBaseOnWidth = %s, %s = %f, %s = %f, targetDensity = %f, targetScaledDensity = %f, targetDensityDpi = %d, targetXdpi = %f, targetScreenWidthDp = %d, targetScreenHeightDp = %d",
                    activity!!.javaClass.name,
                    activity.javaClass.simpleName,
                    isBaseOnWidth,
                    if (isBaseOnWidth) "designWidthInDp"
                    else "designHeightInDp",
                    sizeInDp,
                    if (isBaseOnWidth) "designWidthInSubunits" else "designHeightInSubunits",
                    subunitsDesignSize,
                    targetDensity,
                    targetScaledDensity,
                    targetDensityDpi,
                    targetXdpi,
                    targetScreenWidthDp,
                    targetScreenHeightDp
                )
            )
        }

        /**
         * 取消适配
         *
         * @param activity [Activity]
         */
        fun cancelAdapt(activity: Activity?) {
            Preconditions.checkMainThread()
            var initXdpi: Float = AutoSizeConfig.instance!!.initXdpi
            when (AutoSizeConfig.instance!!.unitsManager.supportSubunits) {
                Subunits.PT -> initXdpi /= 72f
                Subunits.MM -> initXdpi /= 25.4f
                else -> {}
            }
            setDensity(
                activity, AutoSizeConfig.instance!!.initDensity,
                AutoSizeConfig.instance!!.initDensityDpi,
                AutoSizeConfig.instance!!.initScaledDensity,
                initXdpi
            )
            setScreenSizeDp(
                activity,
                AutoSizeConfig.instance!!.initScreenWidthDp,
                AutoSizeConfig.instance!!.initScreenHeightDp
            )
        }

        /**
         * 当 App 中出现多进程，并且您需要适配所有的进程，就需要在 App 初始化时调用 [.initCompatMultiProcess]
         * 建议实现自定义 [Application] 并在 [Application.onCreate] 中调用 [.initCompatMultiProcess]
         *
         * @param context [Context]
         */
        fun initCompatMultiProcess(context: Context) {
            context.contentResolver.query(
                Uri.parse("content://" + context.packageName + ".autosize-init-provider"),
                null,
                null,
                null,
                null
            )
        }

        /**
         * 给几大 [DisplayMetrics] 赋值
         *
         * @param activity      [Activity]
         * @param density       [DisplayMetrics.density]
         * @param densityDpi    [DisplayMetrics.densityDpi]
         * @param scaledDensity [DisplayMetrics.scaledDensity]
         * @param xdpi          [DisplayMetrics.xdpi]
         */
        private fun setDensity(
            activity: Activity?,
            density: Float,
            densityDpi: Int,
            scaledDensity: Float,
            xdpi: Float
        ) {
            val activityDisplayMetrics = activity!!.resources.displayMetrics
            setDensity(activityDisplayMetrics, density, densityDpi, scaledDensity, xdpi)
            val appDisplayMetrics: DisplayMetrics? =
                AutoSizeConfig.instance!!.application?.resources
                    ?.getDisplayMetrics()
            if (appDisplayMetrics != null) {
                setDensity(appDisplayMetrics, density, densityDpi, scaledDensity, xdpi)
            }

            //兼容 MIUI
            val activityDisplayMetricsOnMIUI = getMetricsOnMiui(
                activity.resources
            )
            val appDisplayMetricsOnMIUI = AutoSizeConfig.instance!!.application?.resources?.let {
                getMetricsOnMiui(
                    it
                )
            }

            if (activityDisplayMetricsOnMIUI != null) {
                setDensity(activityDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi)
            }
            if (appDisplayMetricsOnMIUI != null) {
                setDensity(appDisplayMetricsOnMIUI, density, densityDpi, scaledDensity, xdpi)
            }
        }

        /**
         * 赋值
         *
         * @param displayMetrics [DisplayMetrics]
         * @param density        [DisplayMetrics.density]
         * @param densityDpi     [DisplayMetrics.densityDpi]
         * @param scaledDensity  [DisplayMetrics.scaledDensity]
         * @param xdpi           [DisplayMetrics.xdpi]
         */
        private fun setDensity(
            displayMetrics: DisplayMetrics,
            density: Float,
            densityDpi: Int,
            scaledDensity: Float,
            xdpi: Float
        ) {
            if (AutoSizeConfig.instance!!.unitsManager.isSupportDP) {
                displayMetrics.density = density
                displayMetrics.densityDpi = densityDpi
            }
            if (AutoSizeConfig.instance!!.unitsManager.isSupportSP) {
                displayMetrics.scaledDensity = scaledDensity
            }
            when (AutoSizeConfig.instance!!.unitsManager.supportSubunits) {
                Subunits.NONE -> {}
                Subunits.PT -> displayMetrics.xdpi = xdpi * 72f
                Subunits.IN -> displayMetrics.xdpi = xdpi
                Subunits.MM -> displayMetrics.xdpi = xdpi * 25.4f
                else -> {}
            }
        }

        /**
         * 给 [Configuration] 赋值
         *
         * @param activity       [Activity]
         * @param screenWidthDp  [Configuration.screenWidthDp]
         * @param screenHeightDp [Configuration.screenHeightDp]
         */
        private fun setScreenSizeDp(activity: Activity?, screenWidthDp: Int, screenHeightDp: Int) {
            if (AutoSizeConfig.instance!!.unitsManager
                    .isSupportDP && AutoSizeConfig.instance!!.unitsManager
                    .isSupportScreenSizeDP
            ) {
                val activityConfiguration = activity!!.resources.configuration
                setScreenSizeDp(activityConfiguration, screenWidthDp, screenHeightDp)

                val appConfiguration: Configuration? =
                    AutoSizeConfig.instance!!.application?.resources
                        ?.configuration
                if (appConfiguration == null) return
                setScreenSizeDp(appConfiguration, screenWidthDp, screenHeightDp)
            }
        }

        /**
         * Configuration赋值
         *
         * @param configuration  [Configuration]
         * @param screenWidthDp  [Configuration.screenWidthDp]
         * @param screenHeightDp [Configuration.screenHeightDp]
         */
        private fun setScreenSizeDp(
            configuration: Configuration,
            screenWidthDp: Int,
            screenHeightDp: Int
        ) {
            configuration.screenWidthDp = screenWidthDp
            configuration.screenHeightDp = screenHeightDp
        }

        /**
         * 解决 MIUI 更改框架导致的 MIUI7 + Android5.1.1 上出现的失效问题 (以及极少数基于这部分 MIUI 去掉 ART 然后置入 XPosed 的手机)
         * 来源于: https://github.com/Firedamp/Rudeness/blob/master/rudeness-sdk/src/main/java/com/bulong/rudeness/RudenessScreenHelper.java#L61:5
         *
         * @param resources [Resources]
         * @return [DisplayMetrics], 可能为 `null`
         */
        private fun getMetricsOnMiui(resources: Resources): DisplayMetrics? {
            if (AutoSizeConfig.instance?.isMiui == true && AutoSizeConfig.instance?.tmpMetricsField != null) {
                return try {
                    AutoSizeConfig.instance!!.tmpMetricsField!!.get(resources) as? DisplayMetrics
                } catch (e: Exception) {
                    null
                }
            }
            return null
        }
    }
}
