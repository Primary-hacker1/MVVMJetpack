package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import com.just.machine.model.OrderListData
import retrofit2.http.Query
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

    suspend fun orderList(
        page: Int?,
        limit: Int?,
        showType: Int?,
        sort: String?,
        order: String?,
        start: String?,
        end: String?
    ): NewResponse<OrderListData> =
        apiService.orderList(
            page,
            limit,
            showType,
            sort,
            order,
            start,
            end
        )

}