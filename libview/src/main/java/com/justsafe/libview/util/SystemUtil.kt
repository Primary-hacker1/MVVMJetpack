package com.justsafe.libview.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt

/**
 * 视图工具类
 * Created by kang on 2017/9/8.
 */
object SystemUtil {
    private const val TAG = "SystemUtil"
    private const val DEVICE_MEIZU = "Meizu"
    private const val DEVICE_XIAOMI = "Xiaomi"
    fun setLightStatusBar(activity: Activity, @ColorInt barColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                Log.i(TAG, "setLightStatusBar: MIUI")
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                Log.i(TAG, "setLightStatusBar: Flyme")
            } else {
                setLightStatusBar(activity)
                Log.i(TAG, "setLightStatusBar: Android M")
            }
            activity.window.statusBarColor = barColor
        }
    }

    fun setDarkStatusBar(activity: Activity, @ColorInt barColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MIUISetStatusBarLightMode(activity, false)) {
                Log.i(TAG, "setLightStatusBar: MIUI")
            } else if (FlymeSetStatusBarLightMode(activity.window, false)) {
                Log.i(TAG, "setLightStatusBar: Flyme")
            } else {
                clearLightStatusBar(activity)
                Log.i(TAG, "setLightStatusBar: Android M")
            }
            activity.window.statusBarColor = barColor
        }
    }

    fun setLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    fun clearLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    /**
     * 先清除之前设置的状态栏透明，然后再设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    fun clearAndSetStatusBarColor(activity: Activity, @ColorInt color: Int) {
        //移除之前的设置
//        if (DeviceUtils.getManufacturer().equals(DEVICE_MEIZU)) {
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            activity.getWindow().getDecorView().setSystemUiVisibility(0);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //重新设置
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) //添加此flag之后，设置状态栏颜色才起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setLightStatusBar(activity, color)
        }
    }

    /**
     * 设置状态栏文字以及图标颜色为浅色
     */
    fun setStatusBarCharacterColor(activity: Activity) {
        //设置状态栏文字颜色及图标为浅色
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    /**
     * 设置状态栏文字以及图标颜色为深色
     */
    fun setStatusBarCharacterDarkColor(activity: Activity) {
        //设置状态栏文字颜色及图标为深色
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param activity 需要设置的activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)
                }
                result = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        setLightStatusBar(activity)
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
                //非MIUI
//                e.printStackTrace();
            }
        }
        return result
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
                //非魅族系统Flyme
            }
        }
        return result
    }

    /**
     * 隐藏状态栏
     *
     * @param activity activity
     */
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (activity.currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        }
    }

    /**
     * 取得状态栏高度
     *
     * @param context context
     * @return 高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 设置状态栏的颜色
     *
     * @param context context
     * @param color   color
     */
    fun setStatusBarColor(context: Activity, color: Int) {
        val w = context.window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        val statusBarHeight = getStatusBarHeight(context)
        val view = View(context)
        view.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams.height = statusBarHeight
        (w.decorView as ViewGroup).addView(view)
        view.setBackgroundColor(color)
    }

    /**
     * 进入/退出沉浸模式
     *
     * @param activity  activity
     * @param immersive immersive
     */
    fun immersive(activity: Activity, immersive: Boolean) {
        val decorView = activity.window.decorView
        if (decorView != null) {
            decorView.systemUiVisibility =
                if (immersive) enterImmersiveUiFlags() else exitImmersiveUiFlags()
        }
    }

    /**
     * 进入沉浸模式的Flags
     */
    private fun enterImmersiveUiFlags(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    /**
     * 退出沉浸的Flags
     */
    private fun exitImmersiveUiFlags(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (hasNavBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    @SuppressLint("DiscouragedApi")
    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag
            val sNavBarOverride = navBarOverride
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    private val navBarOverride: String?
        @SuppressLint("PrivateApi")
        get() {
            var sNavBarOverride: String? = null
            try {
                val c = Class.forName("android.os.SystemProperties")
                val m = c.getDeclaredMethod("get", String::class.java)
                m.isAccessible = true
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
            } catch (e: Throwable) {
            }
            return sNavBarOverride
        }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    fun backgroundAlpha(context: Context, bgAlpha: Float) {
        val lp = (context as Activity).window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        context.window.attributes = lp
    }
}