package com.example.superrestoration_client.view_model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.utils.Common

class CompareFragmentViewModel : ViewModel() {
    private val TAG = "CompareFragmentViewModel"

    private var restoreRequestStatus: MutableLiveData<Int> = MutableLiveData()
    private var imageRequestCode: MutableLiveData<Boolean> = MutableLiveData()
    private var images: HashMap<String, Bitmap?> = hashMapOf()

    init {
        restoreRequestStatus.value = -1
        imageRequestCode.value = false
    }

    fun isPicturesOK(context: Context, models: MutableSet<String>, scale: String, urls: HashMap<String, String>): Boolean {
        models.add("bicubic")
        for(model in models){
            val url = urls["resultDir"]+model+'/'+scale+'/'+urls["name"]
            Common().getBitmapFromUrl(TAG, context, url.replace(".", "_finish."))
            val img = Common().getBitmapFromUrl(TAG, context, url)
            try {
                img?.height
            }catch (e: Exception){
                imageRequestCode.postValue(false)
                Log.e(TAG, "图片不存在!!")
                return false
            }
            images[model] = img
        }
        imageRequestCode.postValue(true)
        return true
    }
    fun getImageRequestCode(): MutableLiveData<Boolean> { return imageRequestCode }
    fun getImages(): HashMap<String, Bitmap?> { return images }
}