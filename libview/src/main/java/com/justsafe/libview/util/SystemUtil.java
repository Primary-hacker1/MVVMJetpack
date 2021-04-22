package com.justsafe.libview.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 视图工具类
 * Created by kang on 2017/9/8.
 */
public class SystemUtil {

    private static final String TAG = "SystemUtil";

    private static final String DEVICE_MEIZU = "Meizu";
    private static final String DEVICE_XIAOMI = "Xiaomi";

    public static void setLightStatusBar(Activity activity, @ColorInt int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                Log.i(TAG, "setLightStatusBar: MIUI");
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                Log.i(TAG, "setLightStatusBar: Flyme");
            } else {
                setLightStatusBar(activity);
                Log.i(TAG, "setLightStatusBar: Android M");
            }

            activity.getWindow().setStatusBarColor(barColor);
        }
    }

    public static void setDarkStatusBar(Activity activity, @ColorInt int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MIUISetStatusBarLightMode(activity, false)) {
                Log.i(TAG, "setLightStatusBar: MIUI");
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), false)) {
                Log.i(TAG, "setLightStatusBar: Flyme");
            } else {
                clearLightStatusBar(activity);
                Log.i(TAG, "setLightStatusBar: Android M");
            }

            activity.getWindow().setStatusBarColor(barColor);
        }
    }

    public static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    public static void clearLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    /**
     * 先清除之前设置的状态栏透明，然后再设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    public static void clearAndSetStatusBarColor(Activity activity, @ColorInt int color) {
        //移除之前的设置
//        if (DeviceUtils.getManufacturer().equals(DEVICE_MEIZU)) {
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            activity.getWindow().getDecorView().setSystemUiVisibility(0);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //重新设置
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//添加此flag之后，设置状态栏颜色才起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtil.setLightStatusBar(activity, color);
        }
    }

    /**
     * 设置状态栏文字以及图标颜色为浅色
     */
    public static void setStatusBarCharacterColor(Activity activity) {
        //设置状态栏文字颜色及图标为浅色
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    /**
     * 设置状态栏文字以及图标颜色为深色
     */
    public static void setStatusBarCharacterDarkColor(Activity activity) {
        //设置状态栏文字颜色及图标为深色
        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param activity 需要设置的activity
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        setLightStatusBar(activity);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                //非MIUI
//                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                //非魅族系统Flyme
            }
        }
        return result;
    }

    /**
     * 隐藏状态栏
     *
     * @param activity activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 取得状态栏高度
     *
     * @param context context
     * @return 高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏的颜色
     *
     * @param context context
     * @param color   color
     */
    public static void setStatusBarColor(Activity context, int color) {
        Window w = context.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = getStatusBarHeight(context);

        View view = new View(context);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getLayoutParams().height = statusBarHeight;
        ((ViewGroup) w.getDecorView()).addView(view);

        view.setBackgroundColor(color);
    }

    /**
     * 进入/退出沉浸模式
     *
     * @param activity  activity
     * @param immersive immersive
     */
    public static void immersive(Activity activity, boolean immersive) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(immersive ? enterImmersiveUiFlags() : exitImmersiveUiFlags());
        }
    }

    /**
     * 进入沉浸模式的Flags
     */
    private static int enterImmersiveUiFlags() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
    }

    /**
     * 退出沉浸的Flags
     */
    private static int exitImmersiveUiFlags() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Context context, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

}
