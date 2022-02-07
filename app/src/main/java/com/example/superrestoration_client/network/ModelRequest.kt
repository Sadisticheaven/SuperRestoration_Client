package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.Model
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ModelRequest {
    @GET("model/findAll")
    fun getModels(): Call<List<Model>>

}