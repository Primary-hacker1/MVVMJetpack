package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import com.just.machine.model.OrderListData
import javax.inject.Inject

/**
 *create by 2020/3/19
 *@author zt
 */
class UserRepository @Inject internal constructor(private val apiService: BaseApiService) {
    /**
     * 协程请求
     */
    suspend fun login(loginBean: LoginBean): NewResponse<LoginData> = apiService.login(loginBean)

//    suspend fun orderList(
//        page: Int? = 1,
//        limit: Int? = 50,
//        showType: String? = "0",//0全部
//        shipSn: String? = "",
//        goodsId: String? = "",
//        brandId: String? = "",
//        mobile: String? = "",
//        orderSn: String? = "",
//        timeArray: List<String>? = ArrayList(),
//        orderStatusArray: List<String>? = ArrayList(),
//        sort: String? = "add_time",
//        order: String? = "desc"
//    ): NewResponse<OrderListData> =
//        apiService.orderList(
//            page,
//            limit,
//            showType,
//            shipSn,
//            goodsId,
//            brandId,
//            mobile,
//            orderSn,
//            timeArray,
//            orderStatusArray,
//            sort,
//            order
//        )

    suspend fun orderList(): NewResponse<OrderListData> =
        apiService.orderList()

}