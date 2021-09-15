package com.just.news.api

import com.just.news.model.NewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *create by 2020/3/19
 *@author zt
 */
interface BaseApiService {

    @GET("/nc/article/headline/{id}/0-10.html")
    suspend fun getNews(@Path("id") id : String?): NewResponse

    @GET("/nc/article/headline/{id}/0-10.html")
    fun getRxNews(@Path("id") id : String?): Single<NewResponse>
}