package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *create by 2020/3/19
 *@author zt
 */
interface BaseApiService {

    @POST("auth/login")
    suspend fun login(@Body loginBean : LoginBean?): NewResponse<LoginData>

}