package com.just.machine.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.common.viewmodel.BaseViewModel
import com.common.viewmodel.LiveDataEvent
import com.just.machine.api.UserRepository
import com.just.machine.dao.Plant
import com.just.machine.dao.PlantRepository
import com.just.machine.model.LoginBean
import com.just.machine.model.OrderListBean
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


    /**
     *@param type 协程请求->直接获取结果的
     */
    fun getDates(type: String) {

        viewModelScope.launch {
            val plants: MutableList<Plant> = ArrayList()
            val plant = Plant("123", "数据库操作", "", 6, 7, "http//：www.baidu.com")
            plants.add(plant)
            plantDao.insertAll(plants)
        }


    }

    fun login(loginBean: LoginBean) {//登陆
        async({ repository.login(loginBean) }, {
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.LOGIN_SUCCESS,
                it.data
            )
        }, {
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.LOGIN_FAIL,
                it
            )
        }, {

        })
    }

    fun orderList() {//查询所有订单
        async({
            repository.orderList()
        }, {
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.ORDERLIST_SUCCESS,
                it.data
            )
        }, {
            mEventHub.value = LiveDataEvent(
                LiveDataEvent.ORDERLIST_FAIL,
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

}



