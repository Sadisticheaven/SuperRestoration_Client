package com.example.superrestoration_client.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface RestorationRequest {
    @POST("process/run")
    @FormUrlEncoded
    fun runModelsOnDatasets(@Field("userId") userId:Int,
                            @Field("modelsId") modelsId:String,
                            @Field("datasetsId") datasetsId:String): Call<Int>

    @Multipart
    @POST("process/singleImage")
    fun runModelOnPicture(@Part("userId") userId: Int,
                          @Part("modelId") modelId: Int,
                          @Part picture: MultipartBody.Part): Call<Map<String, String>>

    @Multipart
    @POST("process/singleImageMultiModels")
    fun runModelsOnPicture(@Part("userId") userId: Int,
                          @Part("modelsId") modelsId: String,
                          @Part picture: MultipartBody.Part): Call<Map<String, String>>
//@Multipart
//@POST("process/singleImage")
//fun runModelOnPicture(@Part picture: MultipartBody.Part): Call<String>
}