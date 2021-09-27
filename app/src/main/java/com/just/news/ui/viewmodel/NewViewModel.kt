package com.just.news.ui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.common.base.subscribes
import com.common.network.LogUtils
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.news.api.UserRepository
import com.just.news.dao.Plant
import com.just.news.dao.PlantRepository
import com.just.news.model.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * create by 2020/6/19
 *
 * @author zt
 */
@HiltViewModel
class NewViewModel @Inject constructor(
    private var repository: UserRepository,
    private var plantDao: PlantRepository
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param type 协程请求->直接获取结果的
     */
    fun getNews(type: String) {

        viewModelScope.launch {
            val plants: MutableList<Plant> = ArrayList()
            val plant = Plant("123", "张涛的数据库操作", "", 6, 7, "http//：www.baidu.com")
            plants.add(plant)
            plantDao.insertAll(plants)
        }

        async({ repository.getNews(type) }, {
            itemNews.clear()
            itemNews.addAll(it.data)
        }, {
            it.printStackTrace()
        }, {

        })
    }


    fun getPlant() {//数据库查询
        viewModelScope.launch {
            plantDao.getPlant("123").collect {
                mEventHub.value = LiveDataEvent(
                    LiveDataEvent.LOGIN_FAIL,
                    it
                )
            }
        }
    }

    /**
     *@param type 协程请求->带loading的
     */
    fun getNewsLoading(type: String) {
        async({ repository.getNews("") }, {
            //返回结果
        }, true, {}, {})
    }

    /**
     *@param type rxjava请求->直接获取结果的
     */
    fun getRxNews(type: String) {
        repository.getRxNews(type)
            .`as`(auto(this))
            .subscribes({

            }, {

            })
    }
}



