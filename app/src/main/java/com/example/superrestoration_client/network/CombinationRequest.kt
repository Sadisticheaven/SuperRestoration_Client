package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.Combination
import retrofit2.Call
import retrofit2.http.*

interface CombinationRequest {
    @GET("combination/findModelCombByOwnerId")
    fun getUserModelCombination(@Query("userId") userId: Int): Call<List<Combination>>

    @POST("combination/addCombinations")
    @FormUrlEncoded
    fun addCombinations(@Field("userId") userId: Int, @Field("combinationList") combinationList: String): Call<Int>
}