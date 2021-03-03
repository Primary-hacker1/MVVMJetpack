package com.yx.news.app

import android.content.Context
import androidx.multidex.MultiDex
import com.yx.news.di.component.DaggerAppComponent


import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App: DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
      return DaggerAppComponent.builder().application(this).build()
    }
}