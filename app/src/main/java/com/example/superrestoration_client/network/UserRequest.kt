package com.example.superrestoration_client.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserRequest {
    @POST("user/login")
    @FormUrlEncoded
    fun userLogin(@Field("userName") userName:String, @Field("userPwd") userPwd:String): Call<Int>
}