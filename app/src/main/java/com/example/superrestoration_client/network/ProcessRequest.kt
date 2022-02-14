package com.example.superrestoration_client.network

import retrofit2.Call
import retrofit2.http.*

interface ProcessRequest {
    @POST("process/run")
    @FormUrlEncoded
    fun runModelsOnDatasets(@Field("userId") userId:Int,
                            @Field("modelsId") modelsId:String,
                            @Field("datasetsId") datasetsId:String): Call<Int>
}