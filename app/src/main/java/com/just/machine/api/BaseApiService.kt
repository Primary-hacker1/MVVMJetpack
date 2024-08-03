package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import com.just.machine.model.OrderListBean
import com.just.machine.model.OrderListData
import com.just.machine.model.OrdersShipmentsBean
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *create by 2020/3/19
 *@author zt
 */
interface BaseApiService {

    @POST("auth/login")
    suspend fun login(@Body loginBean: LoginBean?): NewResponse<LoginData>

    @GET("order/list")
    suspend fun orderList(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("showType") showType: Int?,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("start") start: String?,
        @Query("end") end: String?
    ): NewResponse<OrderListData>


    @POST("order/batchShip")
    suspend fun ordersShipments(@Body ordersShipmentsBean: OrdersShipmentsBean?): NewResponse<String>

}