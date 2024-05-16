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
import android.content.ComponentCallbacks
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import me.jessyan.autosize.external.ExternalAdaptManager
import me.jessyan.autosize.unit.Subunits
import me.jessyan.autosize.unit.UnitsManager
import me.jessyan.autosize.utils.AutoSizeLog
import me.jessyan.autosize.utils.Preconditions
import me.jessyan.autosize.utils.ScreenUtils
import java.lang.reflect.Field

/**
 * ================================================
 * AndroidAutoSize 参数配置类, 给 AndroidAutoSize 配置一些必要的自定义参数
 *
 *
 * Created by JessYan on 2018/8/8 09:58
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class AutoSizeConfig private constructor() {
    private var mApplication: Application? = null
    /**
     * [ExternalAdaptManager] 用来管理外部三方库 [Activity] 的适配
     *
     * @return [.mExternalAdaptManager]
     */
    /**
     * 用来管理外部三方库 [Activity] 的适配
     */
    val externalAdaptManager: ExternalAdaptManager = ExternalAdaptManager()
    /**
     * [UnitsManager] 用来管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
     *
     * @return [.mUnitsManager]
     */
    /**
     * 用来管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
     */
    val unitsManager: UnitsManager = UnitsManager()
    /**
     * 获取 [.mInitDensity]
     *
     * @return [.mInitDensity]
     */
    /**
     * 最初的 [DisplayMetrics.density]
     */
    var initDensity: Float = -1f
        private set
    /**
     * 获取 [.mInitDensityDpi]
     *
     * @return [.mInitDensityDpi]
     */
    /**
     * 最初的 [DisplayMetrics.densityDpi]
     */
    var initDensityDpi: Int = 0
        private set
    /**
     * 获取 [.mInitScaledDensity]
     *
     * @return [.mInitScaledDensity]
     */
    /**
     * 最初的 [DisplayMetrics.scaledDensity]
     */
    var initScaledDensity: Float = 0f
        private set
    /**
     * 获取 [.mInitXdpi]
     *
     * @return [.mInitXdpi]
     */
    /**
     * 最初的 [DisplayMetrics.xdpi]
     */
    var initXdpi: Float = 0f
        private set
    /**
     * 获取 [.mInitScreenWidthDp]
     *
     * @return [.mInitScreenWidthDp]
     */
    /**
     * 最初的 [Configuration.screenWidthDp]
     */
    var initScreenWidthDp: Int = 0
        private set
    /**
     * 获取 [.mInitScreenHeightDp]
     *
     * @return [.mInitScreenHeightDp]
     */
    /**
     * 最初的 [Configuration.screenHeightDp]
     */
    var initScreenHeightDp: Int = 0
        private set

    /**
     * 设计图上的总宽度, 单位 dp
     */
    private var mDesignWidthInDp = 0

    /**
     * 设计图上的总高度, 单位 dp
     */
    private var mDesignHeightInDp = 0
    /**
     * 返回 [.mScreenWidth]
     *
     * @return [.mScreenWidth]
     */
    /**
     * 设备的屏幕总宽度, 单位 px
     */
    var screenWidth: Int = 0
        private set

    /**
     * 设备的屏幕总高度, 单位 px, 如果 [.isUseDeviceSize] 为 `false`, 屏幕总高度会减去状态栏的高度
     */
    private var mScreenHeight = 0

    /**
     * 状态栏高度, 当 [.isUseDeviceSize] 为 `false` 时, AndroidAutoSize 会将 [.mScreenHeight] 减去状态栏高度
     * AndroidAutoSize 默认使用 [ScreenUtils.getStatusBarHeight] 方法获取状态栏高度
     * AndroidAutoSize 使用者可使用 [.setStatusBarHeight] 自行设置状态栏高度
     */
    private var mStatusBarHeight = 0
    /**
     * 返回 [.isBaseOnWidth]
     *
     * @return [.isBaseOnWidth]
     */
    /**
     * 为了保证在不同高宽比的屏幕上显示效果也能完全一致, 所以本方案适配时是以设计图宽度与设备实际宽度的比例或设计图高度与设备实际高度的比例应用到
     * 每个 View 上 (只能在宽度和高度之中选一个作为基准), 从而使每个 View 的高和宽用同样的比例缩放, 避免在与设计图高宽比不一致的设备上出现适配的 View 高或宽变形的问题
     * [.isBaseOnWidth] 为 `true` 时代表以宽度等比例缩放, `false` 代表以高度等比例缩放
     * [.isBaseOnWidth] 为全局配置, 默认为 `true`, 每个 [Activity] 也可以单独选择使用高或者宽做等比例缩放
     */
    var isBaseOnWidth: Boolean = true
        private set
    /**
     * 返回 [.isUseDeviceSize]
     *
     * @return [.isUseDeviceSize]
     */
    /**
     * 此字段表示是否使用设备的实际尺寸做适配
     * [.isUseDeviceSize] 为 `true` 表示屏幕高度 [.mScreenHeight] 包含状态栏的高度
     * [.isUseDeviceSize] 为 `false` 表示 [.mScreenHeight] 会减去状态栏的高度, 默认为 `true`
     */
    var isUseDeviceSize: Boolean = true
        private set

    /**
     * [.mActivityLifecycleCallbacks] 可用来代替在 BaseActivity 中加入适配代码的传统方式
     * [.mActivityLifecycleCallbacks] 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 [Activity]
     */
    private var mActivityLifecycleCallbacks: ActivityLifecycleCallbacksImpl? = null
    /**
     * 框架是否已经停止运行
     *
     * @return `false` 框架正在运行, `true` 框架已经停止运行
     */
    /**
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     *
     * @see .stop
     * @see .restart
     */
    var isStop: Boolean = false
        private set
    /**
     * 框架是否已经开启支持自定义 Fragment 的适配参数
     *
     * @return `true` 为支持
     */
    /**
     * 是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     */
    var isCustomFragment: Boolean = false
        private set
    /**
     * 获取屏幕方向
     *
     * @return `true` 为纵向, `false` 为横向
     */
    /**
     * 屏幕方向, `true` 为纵向, `false` 为横向
     */
    var isVertical: Boolean = false
        private set
    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 `true`, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 `false`, 则会跟随系统设置中字体大小的改变, 默认为 `false`
     *
     * @return [.isExcludeFontScale]
     */
    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 `true`, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 `false`, 则会跟随系统设置中字体大小的改变, 默认为 `false`
     */
    var isExcludeFontScale: Boolean = false
        private set
    /**
     * 区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
     * 当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效
     *
     * @return 私有的字体大小放大比例
     */
    /**
     * 区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
     * 当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 将此值设为 0 则取消此功能
     */
    var privateFontScale: Float = 0f
        private set
    /**
     * 返回 [.isMiui]
     *
     * @return [.isMiui]
     */
    /**
     * 是否是 Miui 系统
     */
    var isMiui: Boolean = false
        private set
    /**
     * 返回 [.mTmpMetricsField]
     *
     * @return [.mTmpMetricsField]
     */
    /**
     * Miui 系统中的 mTmpMetrics 字段
     */
    var tmpMetricsField: Field? = null
        private set
    /**
     * 返回 [.mOnAdaptListener]
     *
     * @return [.mOnAdaptListener]
     */
    /**
     * 屏幕适配监听器，用于监听屏幕适配时的一些事件
     */
    var onAdaptListener: onAdaptListener? = null

    val application: Application?
        get() {
            Preconditions.checkNotNull(mApplication, "Please call the AutoSizeConfig#init() first")
            return mApplication
        }

    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     *
     * @param application   [Application]
     * @param isBaseOnWidth 详情请查看 [.isBaseOnWidth] 的注释
     * @param strategy      [AutoAdaptStrategy], 传 `null` 则使用 [DefaultAutoAdaptStrategy]
     */
    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法默认使用以宽度进行等比例适配, 如想使用以高度进行等比例适配, 请调用 [.init]
     *
     * @param application [Application]
     */
    /**
     * v0.7.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法使用默认的 [AutoAdaptStrategy] 策略, 如想使用自定义的 [AutoAdaptStrategy] 策略
     * 请调用 [.init]
     *
     * @param application   [Application]
     * @param isBaseOnWidth 详情请查看 [.isBaseOnWidth] 的注释
     */
    @JvmOverloads
    fun init(
        application: Application?,
        isBaseOnWidth: Boolean = true,
        strategy: AutoAdaptStrategy? = null
    ): AutoSizeConfig {
        Preconditions.checkArgument(
            initDensity == -1f,
            "AutoSizeConfig#init() can only be called once"
        )
        Preconditions.checkNotNull(application, "application == null")
        this.mApplication = application
        this.isBaseOnWidth = isBaseOnWidth
        val displayMetrics = Resources.getSystem().displayMetrics
        val configuration = Resources.getSystem().configuration

        //设置一个默认值, 避免在低配设备上因为获取 MetaData 过慢, 导致适配时未能正常获取到设计图尺寸
        //建议使用者在低配设备上主动在 Application#onCreate 中调用 setDesignWidthInDp 替代以使用 AndroidManifest 配置设计图尺寸的方式
        if (instance!!.unitsManager.supportSubunits == Subunits.NONE) {
            mDesignWidthInDp = 360
            mDesignHeightInDp = 640
        } else {
            mDesignWidthInDp = 1080
            mDesignHeightInDp = 1920
        }

        getMetaData(application)
        isVertical =
            application!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenSize = ScreenUtils.getScreenSize(application)
        screenWidth = screenSize[0]
        mScreenHeight = screenSize[1]
        mStatusBarHeight = ScreenUtils.getStatusBarHeight()
        AutoSizeLog.d("designWidthInDp = $mDesignWidthInDp, designHeightInDp = $mDesignHeightInDp, screenWidth = $screenWidth, screenHeight = $mScreenHeight")

        initDensity = displayMetrics.density
        initDensityDpi = displayMetrics.densityDpi
        initScaledDensity = displayMetrics.scaledDensity
        initXdpi = displayMetrics.xdpi
        initScreenWidthDp = configuration.screenWidthDp
        initScreenHeightDp = configuration.screenHeightDp
        application.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) {
                if (newConfig.fontScale > 0) {
                    initScaledDensity =
                        Resources.getSystem().displayMetrics.scaledDensity
                    AutoSizeLog.d("initScaledDensity = $initScaledDensity on ConfigurationChanged")
                }
                isVertical = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
                val screenSize = ScreenUtils.getScreenSize(application)
                screenWidth = screenSize[0]
                mScreenHeight = screenSize[1]
            }

            override fun onLowMemory() {
            }
        })
        AutoSizeLog.d("initDensity = $initDensity, initScaledDensity = $initScaledDensity")
        mActivityLifecycleCallbacks = ActivityLifecycleCallbacksImpl(
            WrapperAutoAdaptStrategy(
                strategy ?: DefaultAutoAdaptStrategy()
            )
        )
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        if ("MiuiResources" == application.resources.javaClass.simpleName || "XResources" == application.resources.javaClass.simpleName) {
            isMiui = true
            try {
                tmpMetricsField = Resources::class.java.getDeclaredField("mTmpMetrics")
                tmpMetricsField?.isAccessible = true
            } catch (e: Exception) {
                tmpMetricsField = null
            }
        }
        return this
    }

    /**
     * 重新开始框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    fun restart() {
        Preconditions.checkNotNull(
            mActivityLifecycleCallbacks,
            "Please call the AutoSizeConfig#init() first"
        )
        synchronized(AutoSizeConfig::class.java) {
            if (isStop) {
                mApplication!!.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                isStop = false
            }
        }
    }

    /**
     * 停止框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    fun stop(activity: Activity?) {
        Preconditions.checkNotNull(
            mActivityLifecycleCallbacks,
            "Please call the AutoSizeConfig#init() first"
        )
        synchronized(AutoSizeConfig::class.java) {
            if (!isStop) {
                mApplication!!.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                AutoSize.Companion.cancelAdapt(activity)
                isStop = true
            }
        }
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy [AutoAdaptStrategy]
     */
    fun setAutoAdaptStrategy(autoAdaptStrategy: AutoAdaptStrategy): AutoSizeConfig {
        Preconditions.checkNotNull(autoAdaptStrategy, "autoAdaptStrategy == null")
        Preconditions.checkNotNull(
            mActivityLifecycleCallbacks,
            "Please call the AutoSizeConfig#init() first"
        )
        mActivityLifecycleCallbacks!!.setAutoAdaptStrategy(
            WrapperAutoAdaptStrategy(
                autoAdaptStrategy
            )
        )
        return this
    }

    /**
     * 设置屏幕适配监听器
     *
     * @param onAdaptListener [onAdaptListener]
     */
    fun setOnAdaptListener(onAdaptListener: onAdaptListener): AutoSizeConfig {
        Preconditions.checkNotNull(onAdaptListener, "onAdaptListener == null")
        this.onAdaptListener = onAdaptListener
        return this
    }

    /**
     * 是否全局按照宽度进行等比例适配
     *
     * @param baseOnWidth `true` 为按照宽度, `false` 为按照高度
     * @see .isBaseOnWidth 详情请查看这个字段的注释
     */
    fun setBaseOnWidth(baseOnWidth: Boolean): AutoSizeConfig {
        isBaseOnWidth = baseOnWidth
        return this
    }

    /**
     * 是否使用设备的实际尺寸做适配
     *
     * @param useDeviceSize `true` 为使用设备的实际尺寸 (包含状态栏), `false` 为不使用设备的实际尺寸 (不包含状态栏)
     * @see .isUseDeviceSize 详情请查看这个字段的注释
     */
    fun setUseDeviceSize(useDeviceSize: Boolean): AutoSizeConfig {
        isUseDeviceSize = useDeviceSize
        return this
    }

    /**
     * 是否打印 Log
     *
     * @param log `true` 为打印
     */
    fun setLog(log: Boolean): AutoSizeConfig {
        AutoSizeLog.setDebug(log)
        return this
    }

    /**
     * 是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     *
     * @param customFragment `true` 为支持
     */
    fun setCustomFragment(customFragment: Boolean): AutoSizeConfig {
        isCustomFragment = customFragment
        return this
    }

    val screenHeight: Int
        /**
         * 返回 [.mScreenHeight]
         *
         * @return [.mScreenHeight]
         */
        get() = if (isUseDeviceSize) mScreenHeight else mScreenHeight - mStatusBarHeight

    val designWidthInDp: Int
        /**
         * 获取 [.mDesignWidthInDp]
         *
         * @return [.mDesignWidthInDp]
         */
        get() {
            Preconditions.checkArgument(
                mDesignWidthInDp > 0,
                "you must set " + KEY_DESIGN_WIDTH_IN_DP + "  in your AndroidManifest file"
            )
            return mDesignWidthInDp
        }

    val designHeightInDp: Int
        /**
         * 获取 [.mDesignHeightInDp]
         *
         * @return [.mDesignHeightInDp]
         */
        get() {
            Preconditions.checkArgument(
                mDesignHeightInDp > 0,
                "you must set " + KEY_DESIGN_HEIGHT_IN_DP + "  in your AndroidManifest file"
            )
            return mDesignHeightInDp
        }

    /**
     * 设置屏幕方向
     *
     * @param vertical `true` 为纵向, `false` 为横向
     */
    fun setVertical(vertical: Boolean): AutoSizeConfig {
        isVertical = vertical
        return this
    }

    /**
     * 是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 `true`, App 内的字体的大小将不会跟随系统设置中字体大小的改变
     * 如果为 `false`, 则会跟随系统设置中字体大小的改变, 默认为 `false`
     *
     * @param excludeFontScale 是否屏蔽
     */
    fun setExcludeFontScale(excludeFontScale: Boolean): AutoSizeConfig {
        isExcludeFontScale = excludeFontScale
        return this
    }

    /**
     * 区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
     * 当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效
     *
     * @param fontScale 字体大小放大的比例, 设为 0 则取消此功能
     */
    fun setPrivateFontScale(fontScale: Float): AutoSizeConfig {
        privateFontScale = fontScale
        return this
    }

    /**
     * 设置屏幕宽度
     *
     * @param screenWidth 屏幕宽度
     */
    fun setScreenWidth(screenWidth: Int): AutoSizeConfig {
        Preconditions.checkArgument(screenWidth > 0, "screenWidth must be > 0")
        this.screenWidth = screenWidth
        return this
    }

    /**
     * 设置屏幕高度
     *
     * @param screenHeight 屏幕高度 (需要包含状态栏)
     */
    fun setScreenHeight(screenHeight: Int): AutoSizeConfig {
        Preconditions.checkArgument(screenHeight > 0, "screenHeight must be > 0")
        mScreenHeight = screenHeight
        return this
    }

    /**
     * 设置全局设计图宽度
     *
     * @param designWidthInDp 设计图宽度
     */
    fun setDesignWidthInDp(designWidthInDp: Int): AutoSizeConfig {
        Preconditions.checkArgument(designWidthInDp > 0, "designWidthInDp must be > 0")
        mDesignWidthInDp = designWidthInDp
        return this
    }

    /**
     * 设置全局设计图高度
     *
     * @param designHeightInDp 设计图高度
     */
    fun setDesignHeightInDp(designHeightInDp: Int): AutoSizeConfig {
        Preconditions.checkArgument(designHeightInDp > 0, "designHeightInDp must be > 0")
        mDesignHeightInDp = designHeightInDp
        return this
    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight 状态栏高度
     */
    fun setStatusBarHeight(statusBarHeight: Int): AutoSizeConfig {
        Preconditions.checkArgument(statusBarHeight > 0, "statusBarHeight must be > 0")
        mStatusBarHeight = statusBarHeight
        return this
    }

    /**
     * 获取使用者在 AndroidManifest 中填写的 Meta 信息
     *
     *
     * Example usage:
     * <pre>
     * <meta-data android:name="design_width_in_dp" android:value="360"></meta-data>
     * <meta-data android:name="design_height_in_dp" android:value="640"></meta-data>
    </pre> *
     *
     * @param context [Context]
     */
    private fun getMetaData(context: Context?) {
        Thread {
            val packageManager = context!!.packageManager
            val applicationInfo: ApplicationInfo
            try {
                applicationInfo = packageManager.getApplicationInfo(
                    context
                        .packageName, PackageManager.GET_META_DATA
                )
                if (applicationInfo?.metaData != null) {
                    if (applicationInfo.metaData.containsKey(KEY_DESIGN_WIDTH_IN_DP)) {
                        mDesignWidthInDp = applicationInfo.metaData[KEY_DESIGN_WIDTH_IN_DP] as Int
                    }
                    if (applicationInfo.metaData.containsKey(KEY_DESIGN_HEIGHT_IN_DP)) {
                        mDesignHeightInDp = applicationInfo.metaData[KEY_DESIGN_HEIGHT_IN_DP] as Int
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }.start()
    }

    companion object {
        @Volatile
        private var sInstance: AutoSizeConfig? = null
        private const val KEY_DESIGN_WIDTH_IN_DP = "design_width_in_dp"
        private const val KEY_DESIGN_HEIGHT_IN_DP = "design_height_in_dp"
        val DEPENDENCY_ANDROIDX: Boolean
        val DEPENDENCY_SUPPORT: Boolean

        init {
            DEPENDENCY_ANDROIDX = findClassByClassName("androidx.fragment.app.FragmentActivity")
            DEPENDENCY_SUPPORT = findClassByClassName("android.support.v4.app.FragmentActivity")
        }

        private fun findClassByClassName(className: String): Boolean {
            var hasDependency: Boolean
            try {
                Class.forName(className)
                hasDependency = true
            } catch (e: ClassNotFoundException) {
                hasDependency = false
            }
            return hasDependency
        }

        val instance: AutoSizeConfig?
            get() {
                if (sInstance == null) {
                    synchronized(AutoSizeConfig::class.java) {
                        if (sInstance == null) {
                            sInstance = AutoSizeConfig()
                        }
                    }
                }
                return sInstance
            }
    }
}
