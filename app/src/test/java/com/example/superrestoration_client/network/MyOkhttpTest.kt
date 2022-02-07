package com.example.superrestoration_client.network

import android.content.ContentValues
import android.util.Log
import okhttp3.*
import okio.FileSystem
import org.junit.Assert.*

import org.junit.Test
import java.io.File
import kotlin.concurrent.thread

class MyOkhttpTest {
    val Url = "http://192.168.0.107:8181/model/findAll"
    private var okHttpClient: OkHttpClient = OkHttpClient()

    @Test
    fun getSync() {
        var myOkhttp = MyOkhttp()
        myOkhttp.getSync(Url)
        var x = 0
    }

    @Test
    fun getAsync() {
    }

    @Test
    fun interceptorTest(){
        // 配置Okhttp，设置缓存和拦截器
        okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor {
                // 预处理
                var request = it.request().newBuilder().addHeader("OS", "Android").build()
                println(request.headers)

                // 后处理
                var response = it.proceed(request)
                return@Interceptor response
            }).build()

        var responseBody = String()
        var request: Request =
            Request.Builder().url(Url).build()
        var call: Call = okHttpClient.newCall(request)
        try {
            var response: Response = call.execute()
            responseBody = response.body!!.string()
            println(responseBody.format())
        }catch (e: Exception){
            Log.e(ContentValues.TAG, "getSync: $Url Failed!!\n$e")
        }
    }

    @Test
    fun cacheTest(){
        // 配置cache
        val cacheDir = "C:\\Users\\10142\\Desktop\\AndroidCache"
        okHttpClient = OkHttpClient().newBuilder()
            .cache(Cache(File(cacheDir), 1024*1024))
            .build()

        var responseBody = String()
        var request: Request =
            Request.Builder().url(Url).build()
        var call: Call = okHttpClient.newCall(request)
        try {
            var response: Response = call.execute()
            responseBody = response.body!!.string()
            println(responseBody.format())
            var fileList = File(cacheDir).listFiles()
            for (it in fileList)
                println(it.absolutePath)
        }catch (e: Exception){
            Log.e(ContentValues.TAG, "getSync: $Url Failed!!\n$e")
        }
    }
}