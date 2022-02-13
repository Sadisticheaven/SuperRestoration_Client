package com.example.superrestoration_client.network

import com.example.superrestoration_client.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserRequest {
    @POST("user/login")
    @FormUrlEncoded
    fun userLogin(@Field("userName") userName:String, @Field("userPwd") userPwd:String): Call<User>

    @GET("user/getUserByName")
    fun getUserInfo(@Query("userName") userName: String): Call<User>
}