package com.justsafe.libview.screen;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 作者：李标
 * 日期  2020/6/12
 * 邮箱：Lb_android@163.com
 * 版本：v1.0
 * 模块：Handler的封装
 * 描述: 子类应为静态内部类
 */
public abstract class BaseHandler<T> extends Handler {

    private final WeakReference<T> mReference;

    public BaseHandler(T t) {
        super(Looper.getMainLooper());
        mReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = mReference.get();
        if (t != null) {
            handleMessage(t, msg);
        }
    }
    protected abstract void handleMessage(T t, Message msg);
}
