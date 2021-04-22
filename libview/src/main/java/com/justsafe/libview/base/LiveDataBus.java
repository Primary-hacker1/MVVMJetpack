package com.justsafe.libview.base;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {
        // 消息总线
        private Map<String, MutableLiveData<Object>> bus;

        // 单例模式（静态内部类法）
        private LiveDataBus(){
            bus = new HashMap<>();
        }
        private static class SingleInstance{
            private static LiveDataBus mInstance = new LiveDataBus();
        }
        public static LiveDataBus get(){
            return SingleInstance.mInstance;
        }

        // 获取消息通道
        public<T> MutableLiveData<T> getChanel(String target,Class<T> type){
            if (!bus.containsKey(target)){
                bus.put(target, new MediatorLiveData<>());
            }
            return (MutableLiveData<T>) bus.get(target);
        }
        public MutableLiveData<Object> getChanel(String target){
            return getChanel(target,Object.class);
        }
}
