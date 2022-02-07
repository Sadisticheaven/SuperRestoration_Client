package com.example.superrestoration_client.view_model

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.network.ModelRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.typeOf

class ModelFragmentViewModel: ViewModel() {
    private var modelList: MutableLiveData<ArrayList<Model>> = MutableLiveData()
    private var requestStatus: MutableLiveData<Int> = MutableLiveData()
    private val baseUrl = "http://192.168.0.107:8182"
    init {
        modelList.value = ArrayList()
        requestStatus.value = -1
    }

    fun loadModels(){
        var retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        var httpBinService = retrofit.create(ModelRequest::class.java)
        var call: Call<List<Model>> = httpBinService.getModels()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful){
                    var res = response.body() as ArrayList<Model>
                    // 此处输出的modelList等值是更新前的值，在观察者回调中输出的才是更新后的
                    modelList.postValue(res)
                    requestStatus.postValue(1)
                }else{
                    requestStatus.postValue(0)
                    Log.e(ContentValues.TAG, "getModels Failed: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                requestStatus.postValue(0)
                Log.e(ContentValues.TAG, "getModels Failed!!")
            }
        })
    }

    fun getModelList(): MutableLiveData<ArrayList<Model>> { return modelList }
    fun getRequestStatus(): MutableLiveData<Int> { return requestStatus }
}