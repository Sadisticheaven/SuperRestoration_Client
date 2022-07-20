package com.example.superrestoration_client.view_model

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.network.ModelRequest
import com.example.superrestoration_client.network.MyNetwork
import com.example.superrestoration_client.network.RestorationRequest
import com.example.superrestoration_client.utils.Common
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ObservableViewModel
import com.example.superrestoration_client.utils.Uri2Path
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MultiAlgorithmFragmentViewModel: ViewModel() {
    private var restoreRequestStatus: MutableLiveData<Int> = MutableLiveData()
    private var selectedModelsName: MutableSet<String> = mutableSetOf()
    private var imagesUrl: HashMap<String, String> = hashMapOf()
    init {
        restoreRequestStatus.value = -1
    }

    fun runModelsOnPicture(context: Context, currentUser: User, currentImage: Uri?,
                           modelsMap: HashMap<String, HashMap<String, Int>>, scaleSelected: String): Boolean {
        val userId = currentUser.getUserId()
        val modelsId = arrayListOf<Int>()
        if (selectedModelsName.isNotEmpty()) {
            for (name in selectedModelsName){
                modelsId.add(modelsMap[name]?.get(scaleSelected) ?: return false)
            }
        }else return false

        if (currentImage == Uri.EMPTY) return false
        val picturePart = Common().getRequestPartFromUri(context, currentImage!!, "picture")

        val request = MyNetwork().getHttpService(RestorationRequest::class.java)

        var modelsIdJson = Gson().toJson(modelsId)

        val callback = request.runModelsOnPicture(userId, modelsIdJson, picturePart)
        callback.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                val res = response.body()
                if (res == null) {
                    restoreRequestStatus.postValue(0)
                    return
                }
                val code = res["code"]!!.toInt()
                restoreRequestStatus.postValue(code)
                var value = res["resultDir"]
                if (value != null) {
                    imagesUrl["resultDir"] = value
                }
                value = res["name"]
                if (value != null) {
                    imagesUrl["name"] = value
                }
                Log.e(TAG, "code: $code, res: $imagesUrl")
            }
            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e(TAG, "connect server failed!!")
            }
        })
        return true
    }

    fun getRestoreRequestStatus(): MutableLiveData<Int> { return restoreRequestStatus }
    fun addModel(name: String): Boolean { return selectedModelsName.add(name) }
    fun removeModel(name: String): Boolean { return selectedModelsName.remove(name) }
    fun getSelectedModelsName(): MutableSet<String>? {return selectedModelsName}
    fun getImagesUrl(): HashMap<String, String> {return imagesUrl}

}