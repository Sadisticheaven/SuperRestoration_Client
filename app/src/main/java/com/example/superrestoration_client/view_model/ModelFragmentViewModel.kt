package com.example.superrestoration_client.view_model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Combination
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.network.ModelRequest
import com.example.superrestoration_client.utils.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelFragmentViewModel: ViewModel() {
    private var requestStatus: MutableLiveData<Int> = MutableLiveData()
    private var newCombination: MutableLiveData<Combination> = MutableLiveData()
    init {
        requestStatus.value = -1
        newCombination.value = Combination()
    }

    fun loadModels(shareViewModel: MainActivityShareViewModel) {
        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        val httpBinService = retrofit.create(ModelRequest::class.java)
        val call: Call<List<Model>> = httpBinService.getModels()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful){
                    val res = response.body() as ArrayList<Model>
                    // 此处输出的modelList等值是更新前的值，在观察者回调中输出的才是更新后的
                    shareViewModel.getModelList().postValue(res)
                    requestStatus.postValue(1)
                }else{
                    requestStatus.postValue(0)
                    Log.e(TAG, "getModels Failed: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                requestStatus.postValue(0)
                Log.e(TAG, "getModels Failed!!")
            }
        })
    }

    fun getRequestStatus(): MutableLiveData<Int> { return requestStatus }
    fun getNewCombinations(): Combination { return newCombination.value!! }

}