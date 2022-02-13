package com.example.superrestoration_client.view_model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Combination
import com.example.superrestoration_client.model.Dataset
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.network.CombinationRequest
import com.example.superrestoration_client.utils.Config
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityShareViewModel : ViewModel() {
    private var currentUser: MutableLiveData<User> = MutableLiveData()
    private var modelList: MutableLiveData<ArrayList<Model>> = MutableLiveData()
    private var selectedModels: MutableLiveData<MutableSet<Int>> = MutableLiveData()

    private var datasetList: MutableLiveData<ArrayList<Dataset>> = MutableLiveData()
    private var selectedDatasets: MutableLiveData<MutableSet<Int>> = MutableLiveData()

    private var userCombinationList: MutableLiveData<HashMap<String, Combination>> = MutableLiveData()
    private var isSelectable: MutableLiveData<Boolean> = MutableLiveData()
    private var fromWhere: MutableLiveData<Int> = MutableLiveData()
    private var requestStatus: MutableLiveData<Int>  = MutableLiveData()

    init {
        currentUser.value = User()
        modelList.value = ArrayList()
        selectedModels.value = mutableSetOf()
        datasetList.value = ArrayList()
        selectedDatasets.value = mutableSetOf()
        userCombinationList.value = hashMapOf()
        requestStatus.value = -1
        isSelectable.value = false
        fromWhere.value = 0
    }

    fun requestUserCombinations(){
        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        val call: Call<List<Combination>> = retrofit.create(CombinationRequest::class.java)
            .getUserModelCombination(currentUser.value!!.getUserId())
        call.enqueue(object : Callback<List<Combination>> {
            override fun onResponse(
                call: Call<List<Combination>>,
                response: Response<List<Combination>>
            ) {
                val res: List<Combination> = response.body()!!
                for (combination in res){
                    userCombinationList.value!![combination.getName()] = combination
                }
                requestStatus.postValue(1)
            }
            override fun onFailure(call: Call<List<Combination>>, t: Throwable) {
                requestStatus.postValue(0)
                Log.e(ContentValues.TAG, "load CombinationList Failed!!")
            }
        })
    }

    fun uploadUserCombinations(){
        val combinations = arrayListOf<Combination>()
        for (combination in userCombinationList.value!!){
            combinations.add(combination.value)
        }

        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        val call: Call<Int> = retrofit.create(CombinationRequest::class.java)
            .addCombinations(currentUser.value!!.getUserId(), Gson().toJson(combinations))
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful)
                    requestStatus.postValue(1)
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                requestStatus.postValue(0)
                Log.e(ContentValues.TAG, "load CombinationList Failed!!")
            }
        })
    }

    fun setCurrentUser(value: User){ currentUser.value = value }
    fun getCurrentUser(): MutableLiveData<User> { return currentUser }

    fun addModel(position: Int): Boolean { return selectedModels.value!!.add(position) }
    fun addDataset(position: Int): Boolean { return selectedDatasets.value!!.add(position) }

    fun removeModel(position: Int): Boolean { return selectedModels.value!!.remove(position) }
    fun removeDataset(position: Int): Boolean { return selectedDatasets.value!!.remove(position) }

    fun getSelectedModels(): MutableSet<Int> { return selectedModels.value!! }
    fun getSelectedDatasets(): MutableSet<Int> { return selectedDatasets.value!! }

    fun getModelList(): MutableLiveData<ArrayList<Model>> { return modelList }
    fun getDatasetList(): MutableLiveData<ArrayList<Dataset>> { return datasetList }
    fun addNewCombination(newCombination: Combination) { userCombinationList.value!![newCombination.getName()] = newCombination }
    fun getCombinations(): HashMap<String, Combination> { return userCombinationList.value!! }
    fun getIsSelectable(): MutableLiveData<Boolean> { return isSelectable }
    fun setIsSelectable(value: Boolean) { isSelectable.value = value }
}