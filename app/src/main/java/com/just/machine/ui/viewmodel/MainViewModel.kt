package com.just.machine.ui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.viewModelScope
import com.common.base.subscribes
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.machine.api.UserRepository
import com.just.machine.dao.Plant
import com.just.machine.dao.PlantRepository
import com.just.machine.model.Data
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
class MainViewModel @Inject constructor(
    private var repository: UserRepository,
    private var plantDao: PlantRepository
) : BaseViewModel() {

    var itemNews: ObservableList<Data> = ObservableArrayList()

    /**
     *@param type 协程请求->直接获取结果的
     */
    fun getDates(type: String) {

        viewModelScope.launch {
            val plants: MutableList<Plant> = ArrayList()
            val plant = Plant("123", "张涛的数据库操作", "", 6, 7, "http//：www.baidu.com")
            plants.add(plant)
            plantDao.insertAll(plants)
        }

        async({ repository.getNews(type) }, {
            itemNews.clear()
            it.data?.let { it1 -> itemNews.addAll(it1) }

            mEventHub.value = it.data?.let { it1 ->
                LiveDataEvent(
                    LiveDataEvent.LOGIN_SUCCESS,
                    it1
                )
            }
        }, {
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.LOGIN_FAIL,
                it
            )
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

    fun getPlant1() = plantDao.getPlant("123")


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



