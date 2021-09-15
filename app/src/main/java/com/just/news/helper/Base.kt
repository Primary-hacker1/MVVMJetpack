package com.just.news.helper

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *create by 2020/7/7
 *@author zt
 */

fun<T> Single<T>.async()=this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())