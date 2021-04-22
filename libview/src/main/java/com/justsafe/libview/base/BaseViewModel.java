package com.justsafe.libview.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * 基类
 */
public abstract class BaseViewModel extends AndroidViewModel {

    protected String TAG = getClass().getSimpleName();

    public SingleLiveEvent<LiveDataEvent> mEventHub = new SingleLiveEvent<>();

    public SingleLiveEvent<LiveDataEvent> getEventHub() {
        return mEventHub;
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
