package com.example.superrestoration_client.view_model


import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.network.ModelRequest
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SuperRestorationActivityViewModel : ViewModel() {
    private val TAG = "SuperRestorationActivityViewModel"
    private var currentImage: MutableLiveData<Uri> = MutableLiveData()
    private var currentUser: MutableLiveData<User> = MutableLiveData()
    private var modelsMap: HashMap<String, HashMap<String, Int>> = hashMapOf()
    private var modelRequestStatus: MutableLiveData<Int> = MutableLiveData()
    private var notify: MutableLiveData<FragmentNotify> = MutableLiveData()
    private var selectedModelsName: MutableSet<String>? = mutableSetOf()
    private var scaleSelected: String = "x2"
    private var imagesUrl: HashMap<String, String>? = hashMapOf()

    init {
        currentUser.value = User()
        currentImage.value = Uri.EMPTY
        modelRequestStatus.value = -1
        notify.value = FragmentNotify(Config.fragmentTag["SuperRestorationActivity"]!!,
            Config.fragmentTag["SuperRestorationActivity"]!!)
    }

    fun loadModels() {
        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        val httpBinService = retrofit.create(ModelRequest::class.java)
        val call: Call<List<Model>> = httpBinService.getModels()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful){
                    val res = response.body() as ArrayList<Model>
//                    modelList.postValue(res)
                    for (model in res){
                        if (modelsMap[model.getModelName()] == null)
                            modelsMap[model.getModelName()] = hashMapOf()
                        modelsMap[model.getModelName()]!!['x' + model.getModelScale().toString()] =
                            model.getModelId()
                    }
                    modelRequestStatus.postValue(1)
                }else{
                    modelRequestStatus.postValue(0)
                    Log.e(ContentValues.TAG, "getModels Failed: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                modelRequestStatus.postValue(0)
                Log.e(ContentValues.TAG, "getModels Failed!!")
            }
        })
    }

    fun getModelRequestStatus(): MutableLiveData<Int> { return modelRequestStatus }
    fun setCurrentUser(value: User){ currentUser.value = value }
    fun getCurrentUser(): User? { return currentUser.value }
    fun getCurrentImage():MutableLiveData<Uri> { return currentImage}
    fun getModelsMap(): HashMap<String, HashMap<String, Int>> {return modelsMap}
    fun getNotify(): MutableLiveData<FragmentNotify> { return notify }
    fun getSelectedModelsName(): MutableSet<String>? {return selectedModelsName}
    fun setSelectedModelsName(value: MutableSet<String>?){selectedModelsName = value}
    fun getImagesUrl(): HashMap<String, String>? {return imagesUrl}
    fun setImagesUrl(value: HashMap<String, String>?)  {imagesUrl = value}
    fun setScaleSelected(value: String){scaleSelected = value}
    fun getScaleSelected(): String {return scaleSelected}
}