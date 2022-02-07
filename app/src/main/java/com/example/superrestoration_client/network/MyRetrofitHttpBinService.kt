package com.example.superrestoration_client.network

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MyRetrofitHttpBinService {
    @GET("model/findAll")
    fun getModels(): Call<ResponseBody>

    @GET("user/findAll")
    fun getUsers(): Call<ResponseBody>

    @GET("{path}")
    fun getUrl(@Path("path", encoded=true) path: String): Call<ResponseBody>

}