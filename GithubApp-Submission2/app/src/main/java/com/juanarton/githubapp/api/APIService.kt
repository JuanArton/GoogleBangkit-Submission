package com.juanarton.githubapp.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {
    @GET("search/users")
    fun getData(
        @Header("User-Agent") ua: String,
        @Query("q") username: String
    ): Call<ResponseBody>

    @GET
    fun getDetail(
        @Header("User-Agent") ua: String,
        @Url url: String
    ): Call<ResponseBody>

    @GET
    fun getFollowers(
        @Header("User-Agent") ua: String,
        @Url url: String
    ): Call<ResponseBody>
}