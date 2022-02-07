package com.example.superrestoration_client.network
import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread


class MyOkhttp {
    private var okHttpClient: OkHttpClient = OkHttpClient()

    fun getSync(Url: String): String {
        var responseBody = String()

//        同步请求需要在子线程中进行
        thread(start = true){
            var request: Request =
                Request.Builder().url(Url).build()
            var call: Call = okHttpClient.newCall(request)
            try {
                var response: Response = call.execute()
                responseBody = response.body!!.string()
                println(responseBody.format())
            }catch (e: Exception){
                Log.e(TAG, "getSync: $Url Failed!!\n$e")
            }
        }
        return responseBody
    }

    fun getAsync(Url: String){
        var request: Request =
            Request.Builder().url(Url).build()
        var call: Call = okHttpClient.newCall(request)
        var response = call.enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "getASync: Failed" )
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    Log.i(TAG, "ok-getASync: " + response.body!!.string())
                }else{
                    Log.i(TAG, "getASync: " + response.code)
                }
            }
        })
    }

    fun postSync(Url: String){

    }
}
