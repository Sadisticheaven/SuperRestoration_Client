package com.example.superrestoration_client.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Dataset
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.ProcessHistory
import com.example.superrestoration_client.network.HistoryRequest
import com.example.superrestoration_client.network.MyNetwork
import com.example.superrestoration_client.network.RestorationRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRestorationViewModel: ViewModel() {
    private val TAG = "ImageRestorationViewModel"
    private var selectedModelsIndex: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    private var selectedDatasetsIndex: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    private var history: MutableLiveData<ArrayList<ProcessHistory>> = MutableLiveData()

    private var restorationRequestCode: MutableLiveData<Int> = MutableLiveData()
    private var historyRequestCode: MutableLiveData<Int> = MutableLiveData()
    init {
        selectedModelsIndex.value = arrayListOf()
        selectedDatasetsIndex.value = arrayListOf()
        history.value = arrayListOf()
        restorationRequestCode.value = -1
        historyRequestCode.value = -1
    }

    fun getSelectedModelsIndex(): MutableLiveData<ArrayList<Int>> { return selectedModelsIndex }
    fun setSelectedModelsIndex(value: ArrayList<Int>){ selectedModelsIndex.value = value }

    fun getSelectedDatasetsIndex(): MutableLiveData<ArrayList<Int>> { return selectedDatasetsIndex }
    fun setSelectedDatasetsPosition(value: ArrayList<Int>){ selectedDatasetsIndex.value = value }

    fun getHistory(): MutableLiveData<ArrayList<ProcessHistory>> { return history }

    fun getRestorationRequestCode(): MutableLiveData<Int> { return restorationRequestCode }
    fun getHistoryRequestCode(): MutableLiveData<Int> { return historyRequestCode }

    fun runModelsOnDatasets(userId: Int, models: ArrayList<Model>, datasets: ArrayList<Dataset>){
        if (selectedModelsIndex.value!!.isEmpty()
            || selectedDatasetsIndex.value!!.isEmpty()){
            restorationRequestCode.postValue(1)
            return
        }
        //请求服务器
        var modelsId = arrayListOf<Int>()
        for (idx in selectedModelsIndex.value!!)
            modelsId.add(models[idx].getModelId())
        val modelsIdString = Gson().toJson(modelsId)

        var datasetsId = arrayListOf<Int>()
        for (idx in selectedDatasetsIndex.value!!)
            datasetsId.add(datasets[idx].getDatasetId())
        val datasetsIdString = Gson().toJson(datasetsId)

        val restorationRequest = MyNetwork().getHttpService(RestorationRequest::class.java)
        val call: Call<Int> = restorationRequest.runModelsOnDatasets(userId, modelsIdString, datasetsIdString)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res: Int = response.body()!!
                restorationRequestCode.postValue(res)
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                restorationRequestCode.postValue(0)
                Log.e(TAG, "Connect Server Failed!!")
            }
        })
    }

    fun refreshHistory(userId: Int){
        val request = MyNetwork().getHttpService(HistoryRequest::class.java)
        val call: Call<ArrayList<ProcessHistory>> = request.getByUserId(userId)
        call.enqueue(object : Callback<ArrayList<ProcessHistory>> {
            override fun onResponse(
                call: Call<ArrayList<ProcessHistory>>,
                response: Response<ArrayList<ProcessHistory>>
            ) {
                var res = response.body()
                if (res == null)
                    res = arrayListOf()
                history.postValue(res)
                historyRequestCode.postValue(1)
            }
            override fun onFailure(call: Call<ArrayList<ProcessHistory>>, t: Throwable) {
                historyRequestCode.postValue(0)
                Log.e(TAG, "Connect Server Failed!!")
            }
        })
    }
}