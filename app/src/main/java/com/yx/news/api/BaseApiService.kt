package com.yx.news.api

import com.yx.news.model.NewResponse
import com.yx.news.model.NewResponses
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *create by 2020/6/19
 *@author yx
 */
interface BaseApiService {

    @GET("/nc/article/headline/{id}/0-10.html")
    suspend fun getNews(@Path("id") id : String?): NewResponses

    @GET("/nc/article/headline/{id}/0-10.html")
    fun getRxNews(@Path("id") id : String?): Single<NewResponses>
}