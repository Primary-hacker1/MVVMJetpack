package com.just.news.ui.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.common.base.subscribes
import com.common.network.LogUtils
import com.common.viewmodel.BaseViewModel
import com.just.news.api.UserRepository
import com.just.news.model.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * create by 2020/6/19
 *
 * @author zt
 */
@HiltViewModel
class NewViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var repository: UserRepository

    var itemNews: ObservableList<Data> = ObservableArrayList()

    //协程请求->直接获取结果的
    fun getNews(type: String) {
        //模拟数据
        for (index in 1 until 10) {
            val bean = Data()
            bean.title = "nice${index}"
            itemNews.add(bean)
        }

        LogUtils.e(tag + itemNews.toString())

//        async({ repository.getNews(type) }, {
//            itemNews.clear()
//            itemNews.addAll(it.list)
//        }, {
//            it.printStackTrace()
//        }, {
//
//        })
    }

    //协程请求->带loading的
    fun getNewsLoading() {
        async({ repository.getNews("") }, {
            //返回结果
        }, true, {}, {})
    }

    //rxjava请求->
    fun getRxNews(type: String) {
        repository.getRxNews(type)
            .`as`(auto(this))
            .subscribes({

            }, {

            })
//            .subscribe({
//                itemNews.clear()
//                itemNews.addAll(it.list)
//            }, {
//                it.printStackTrace()
//            })
    }
}



