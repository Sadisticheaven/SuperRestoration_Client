package com.example.superrestoration_client.network

import com.example.superrestoration_client.utils.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyNetwork {
    fun<T> getHttpService(requestInterface: Class<T>): T{
        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        return retrofit.create(requestInterface)
    }
}