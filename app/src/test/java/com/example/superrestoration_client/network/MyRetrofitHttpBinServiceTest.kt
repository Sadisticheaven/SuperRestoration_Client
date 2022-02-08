package com.example.superrestoration_client.network

import android.content.ContentValues
import android.util.Log
import com.example.superrestoration_client.model.Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.util.ArrayList
import kotlin.concurrent.thread

class MyRetrofitHttpBinServiceTest{
    val baseUrl = "http://192.168.0.107:8181"

    @Test
    fun getModelAsyncTest(){
        var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
        var httpBinService = retrofit.create(MyRetrofitHttpBinService::class.java)
        var call = httpBinService.getModels()
        call.enqueue(object:retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    println(response.body()!!.string())
                }else{
                    Log.i(ContentValues.TAG, "getASync: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        val x = 0
    }

    @Test
    fun getModelSyncTest(){
        var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
        var httpBinService = retrofit.create(MyRetrofitHttpBinService::class.java)

        var call = httpBinService.getModels()

        var response = call.execute()
        val type: Type = object:TypeToken<ArrayList<Model>>(){}.type
        var modelList: ArrayList<Model> = Gson().fromJson(response.body()!!.string(), type)
        println(modelList)

    }

    @Test
    fun getUrlSyncTest(){
        var retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
        var httpBinService = retrofit.create(MyRetrofitHttpBinService::class.java)

        var call = httpBinService.getUrl("model/findAll")
        try {
            var response = call.execute()
            println(response.body()!!.string())
        }catch (e: Exception){
            Log.e(ContentValues.TAG, "getSync Failed!!\n$e")
        }
        val x = 0
    }

    @Test
    fun getModelsConvert(){
        var retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        var httpBinService = retrofit.create(ModelRequest::class.java)

        var call: Call<List<Model>> = httpBinService.getModels()

        var response: Response<List<Model>> = call.execute()
        var modelList: List<Model>? = response.body()
        println(modelList)
    }

    @Test
    fun cacheTest(){
        // 配置Okhttp，设置缓存和拦截器
        // 配置cache
        val cacheDir = "C:\\Users\\10142\\Desktop\\AndroidCache"
        var okHttpClient = OkHttpClient().newBuilder()
            .cache(Cache(File(cacheDir), (1024*1024).toLong()))
            .build()


        var retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        var httpBinService = retrofit.create(ModelRequest::class.java)

        var call: Call<List<Model>> = httpBinService.getModels()

        var response: Response<List<Model>> = call.execute()
        var modelList: List<Model>? = response.body()
        println(modelList)
    }
}