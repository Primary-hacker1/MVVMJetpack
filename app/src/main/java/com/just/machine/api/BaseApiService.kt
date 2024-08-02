package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import com.just.machine.model.OrderListBean
import com.just.machine.model.OrderListData
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

//    @GET("order/list")
//    suspend fun orderList(
//        @Query("page") page: Int? = 1,
//        @Query("limit") limit: Int? = 50,
//        @Query("showType") showType: String? = "0",//0全部
//        @Query("shipSn") shipSn: String? = "",
//        @Query("goodsId") goodsId: String? = "",
//        @Query("brandId") brandId: String? = "",
//        @Query("mobile") mobile: String? = "",
//        @Query("orderSn") orderSn: String? = "",
//        @Query("timeArray") timeArray: List<String>? = ArrayList(),
//        @Query("orderStatusArray") orderStatusArray: List<String>? = ArrayList(),
//        @Query("sort") sort: String? = "add_time",
//        @Query("order") order: String? = "desc"
//    ): NewResponse<OrderListData>

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

}