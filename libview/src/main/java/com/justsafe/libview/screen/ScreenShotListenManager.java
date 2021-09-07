package com.justsafe.libview.screen;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：李标
 * 日期  2020/8/24
 * 邮箱：Lb_android@163.com
 * 版本：v1.0
 * 模块：屏幕截图ListenManager
 * 描述:
 */
public class ScreenShotListenManager {

    private static final String TAG = ScreenShotListenManager.class.getName();

    /**
     * 读取媒体数据库时需要读取的列
     */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };
    /**
     * 读取媒体数据库时需要读取的列, 其中 WIDTH 和 HEIGHT 字段在 API 16 以后才有
     */
    private static final String[] MEDIA_PROJECTIONS_API_16 = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
    };

    /**
     * 截屏依据中的路径判断关键字
     */
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    private static Point sScreenRealSize;

    /**
     * 已回调过的路径
     */
    private final static List<String> sHasCallbackPaths = new ArrayList<>();

    private Context mContext;
    private int height;

    private OnScreenShotListener mListener;

    private long mStartListenTime;

    /**
     * 内部存储器内容观察者
     */
    private MediaContentObserver mInternalObserver;

    /**
     * 外部存储器内容观察者
     */
    private MediaContentObserver mExternalObserver;

    /**
     * 运行在 UI 线程的 Handler, 用于运行监听器回调
     */
    private final MyHandler mUiHandler = new MyHandler(this);

    //    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private static class MyHandler extends BaseHandler<ScreenShotListenManager> {
        public MyHandler(ScreenShotListenManager manager) {
            super(manager);
        }

        @Override
        protected void handleMessage(ScreenShotListenManager manager, Message msg) {
        }
    }

    /***
     * 重新创建Manager对象
     * @param context 应用上下文
     */
    private ScreenShotListenManager(Context context, int height) {
        try {
            if (context == null) {
                return;
            }
            mContext = context;
            this.height = height;
            // 获取屏幕真实的分辨率
            if (sScreenRealSize == null) {
                sScreenRealSize = getRealScreenSize();
                if (sScreenRealSize != null) {
                    Log.e(TAG,"screen Real Size: " + sScreenRealSize.x + " * " + sScreenRealSize.y);
                } else {
                    Log.e(TAG,"Get screen real size failed.");
                }
                Log.e(TAG,"ScreenShotListenManager: ");
            }
        } catch (Exception e) {
            Log.e(TAG,"==The context must not be null.====>");
        }
    }

    public static ScreenShotListenManager newInstance(Context context, int height) {
        assertInMainThread();
        return new ScreenShotListenManager(context, height);
    }

    /**
     * 启动监听
     */
    public void startListen() {
        try {
            assertInMainThread();

//        sHasCallbackPaths.clear();

            // 记录开始监听的时间戳
            mStartListenTime = System.currentTimeMillis();

            // 创建内容观察者
            mInternalObserver = new MediaContentObserver(this, MediaStore.Images.Media.INTERNAL_CONTENT_URI, mUiHandler);
            mExternalObserver = new MediaContentObserver(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mUiHandler);

            // 注册内容观察者
            mContext.getContentResolver().registerContentObserver(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
                    mInternalObserver
            );
            mContext.getContentResolver().registerContentObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q,
                    mExternalObserver
            );
        } catch (Exception ignored) {
        }
    }

    /**
     * 停止监听
     */
    public void stopListen() {
        try {
            assertInMainThread();
            // 注销内容观察者
            if (mInternalObserver != null) {
                try {
                    mContext.getContentResolver().unregisterContentObserver(mInternalObserver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mInternalObserver = null;
            }
            if (mExternalObserver != null) {
                try {
                    mContext.getContentResolver().unregisterContentObserver(mExternalObserver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mExternalObserver = null;
            }
            // 清空数据
            mStartListenTime = 0;
//        sHasCallbackPaths.clear();
            //切记！！！:必须设置为空 可能mListener 会隐式持有Activity导致释放不掉
            mListener = null;
        } catch (Exception ignored) {
        }

    }

    /**
     * MediaStore.Images.ImageColumns.DATE_ADDED 默认的方式 //添加到数据库的时间，单位秒
     * MediaStore.Images.ImageColumns.DATE_TAKEN和MediaStore.Images.ImageColumns.DATE_MODIFIED方式一样的效果
     * MediaStore.Images.ImageColumns.DATE_MODIFIED  date_modified	文件最后修改时间，单位秒
     * 处理媒体数据库的内容改变
     */
    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            if (mContext == null) {
                return;
            }
            ContentResolver mContentResolver = mContext.getContentResolver();
            if (mContentResolver == null) {
                return;
            }
            // 数据改变时查询数据库中最后加入的一条数据
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                Bundle bundle = createSqlQueryBundle(
                );

                cursor = mContentResolver.query(contentUri
                        , MEDIA_PROJECTIONS_API_16
                        , bundle
                        , null);

            } else cursor = mContentResolver.query(
                    contentUri,
                    MEDIA_PROJECTIONS_API_16,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " desc limit 1"
            );
            if (cursor == null) {
                Log.e(TAG,"Deviant logic.");
                return;
            }
            if (!cursor.moveToFirst()) {
                Log.i(TAG,"Cursor no data.");
                return;
            }
            Log.i(TAG,"+screen+handleMediaContentChange: ====获取截图图片地址成功=>");

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            int widthIndex;
            int heightIndex;
            widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
            heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            int width;
            int height;
            if (widthIndex >= 0 && heightIndex >= 0) {
                width = cursor.getInt(widthIndex);
                height = cursor.getInt(heightIndex);
            } else {
                // API 16 之前, 宽高要手动获取
                Point size = getImageSize(data);
                assert size != null;
                width = size.x;
                height = size.y;
            }

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, width, height);

        } catch (Exception e) {
            Log.i(TAG,"screen=获取系统截图异常=Exception=>" + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * Android 11 及以上使用的查询条件参数方式
     *
     * @return Bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bundle createSqlQueryBundle() {
        Bundle queryArgs = new Bundle();
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "datetaken DESC ");
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, 1 + "");
        return queryArgs;
    }

    /***
     * @param imagePath API 16 之前, 宽高要手动获取
     * @return -
     */
    private Point getImageSize(String imagePath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            return new Point(options.outWidth, options.outHeight);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 处理获取到的一行数据
     */
    private void handleMediaRowData(String data, long dateTaken, int width, int height) {
        Log.i(TAG,"handleMediaRowData: ===data===>" + data);
        if (checkScreenShot(data, dateTaken, width, height)) {
            Log.i(TAG,"ScreenShot: path = " + data + "; size = " + width + " * " + height
                    + "; date = " + dateTaken);
            if (mListener != null && !checkCallback(data)) {
                mListener.onShot(data);
            }
        } else {
            // 如果在观察区间媒体数据库有数据改变，又不符合截屏规则，则输出到 log 待分析
            Log.i(TAG,"Media content changed, but not screenshot: path = " + data
                    + "; size = " + width + " * " + height + "; date = " + dateTaken);
        }
    }

    /**
     * 判断指定的数据行是否符合截屏条件
     */
    private boolean checkScreenShot(String data, long dateTaken, int width, int height) {
        /*
         * 判断依据一: 时间判断
         */
        // 如果加入数据库的时间在开始监听之前, 或者与当前时间相差大于10秒, 则认为当前没有截屏
        if (dateTaken < mStartListenTime || (System.currentTimeMillis() - dateTaken) > 10 * 1000) {
            return false;
        }

        /*
         * 判断依据二: 尺寸判断
         */
        if (sScreenRealSize != null) {
            // 如果图片尺寸超出屏幕, 则认为当前没有截屏
            if (!((width <= sScreenRealSize.x && height <= sScreenRealSize.y)
                    || (height <= sScreenRealSize.x && width <= sScreenRealSize.y))) {
                return false;
            }
        }

        /*
         * 判断依据三: 路径判断
         */
        if (TextUtils.isEmpty(data)) {
            return false;
        }
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否已回调过, 某些手机ROM截屏一次会发出多次内容改变的通知; <br/>
     * 删除一个图片也会发通知, 同时防止删除图片时误将上一张符合截屏规则的图片当做是当前截屏.
     */
    private boolean checkCallback(String imagePath) {
        if (sHasCallbackPaths.contains(imagePath)) {
            Log.i(TAG,"ScreenShot: imgPath has done"
                    + "; imagePath = " + imagePath);
            return true;
        }
        // 大概缓存15~20条记录便可
        if (sHasCallbackPaths.size() >= 20) {
            int i;
            for (i = 0; i < 5; i++) {
                sHasCallbackPaths.remove(0);
            }
        }
        sHasCallbackPaths.add(imagePath);
        return false;
    }

    /**
     * 获取屏幕分辨率
     */
    private Point getRealScreenSize() {
        Point screenSize = null;
        try {
            screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                DisplayMetrics metrics2 = mContext.getResources().getDisplayMetrics();
                screenSize.set(metrics2.widthPixels, this.height);
            } else {
                try {
                    WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    Display defaultDisplay = windowManager.getDefaultDisplay();
                    defaultDisplay.getRealSize(screenSize);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return screenSize;
    }

    /**
     * 设置截屏监听器
     */
    public void setListener(OnScreenShotListener listener) {
        mListener = listener;
    }

    public interface OnScreenShotListener {
        void onShot(String imagePath);
    }

    /**
     * 判断是否主线程
     */
    private static void assertInMainThread() {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                String methodMsg = null;
                if (elements.length >= 4) {
                    methodMsg = elements[3].toString();
                }
                Log.i(TAG,"assertInMainThread: ==Call the method must be in main thread:" + methodMsg);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private static class MediaContentObserver extends ContentObserver {
        private final WeakReference<ScreenShotListenManager> managerWeakReference;
        private final Uri mContentUri;

        public MediaContentObserver(ScreenShotListenManager manager, Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
            managerWeakReference = new WeakReference<>(manager);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (managerWeakReference != null) {
                ScreenShotListenManager manager = managerWeakReference.get();
                if (manager != null) {
                    manager.handleMediaContentChange(mContentUri);
                }
            }
        }
    }

}
