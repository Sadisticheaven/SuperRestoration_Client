package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.ProcessHistory
import retrofit2.Call
import retrofit2.http.*

interface HistoryRequest {
    @GET("history/getByUserId")
    fun getByUserId(@Query("userId") userId:Int): Call<ArrayList<ProcessHistory>>

    @GET("history/getImagesUrl")
    fun getImagesUrl(@Query("historyId") historyId:Int,
                     @Query("modelId") modelId:Int,
                     @Query("datasetId") datasetId:Int): Call<ArrayList<String>>

}