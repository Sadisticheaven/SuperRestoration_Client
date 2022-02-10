package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.Dataset
import retrofit2.Call
import retrofit2.http.GET

interface DatasetRequest {
    @GET("dataset/findAll")
    fun getDatasets(): Call<List<Dataset>>

}