package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.Model
import retrofit2.Call
import retrofit2.http.GET

interface ModelRequest {
    @GET("model/findAll")
    fun getModels(): Call<List<Model>>

}