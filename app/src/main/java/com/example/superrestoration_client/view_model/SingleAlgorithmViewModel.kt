package com.example.superrestoration_client.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.network.MyNetwork
import com.example.superrestoration_client.network.RestorationRequest
import com.example.superrestoration_client.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SingleAlgorithmViewModel : ViewModel() {
    private val TAG = "SingleAlgorithmViewModel"

    private var restoreRequestStatus: MutableLiveData<Int> = MutableLiveData()
    private var notify: MutableLiveData<FragmentNotify> = MutableLiveData()
    private var imageRequestParams = hashMapOf<String, String>()
    private var imageRequestCode: MutableLiveData<Boolean> = MutableLiveData()
    private var imagesUrl: HashMap<String, String> = hashMapOf()
    private var imgSR: Bitmap? = null
    private var imgLR: Bitmap? = null

    init {
        restoreRequestStatus.value = -1
        imageRequestCode.value = false
        notify.value = FragmentNotify(Config.fragmentTag["MainActivity"]!!, Config.fragmentTag["MainActivity"]!!)
    }

    fun runModelOnPicture(context: Context, currentUser: User, currentImage: Uri?, modelsMap: HashMap<String, HashMap<String, Int>>){
        val userId = currentUser.getUserId()
        val modelId: Int
        if (imageRequestParams["modelName"] != null) {
            val model = modelsMap[imageRequestParams["modelName"]]
            if (model != null && imageRequestParams["modelScale"] != null) {
                modelId = model[imageRequestParams["modelScale"]] ?: return
            }else return
        }else return

        if (currentImage == Uri.EMPTY) return
        var path = Uri2Path.getFilePathByUri(context, currentImage)
        if (path == null){
            path = Uri2Path.getRealPathFromURI(context, currentImage)
        }

        val picture = File(path)
        if(!picture.exists())
            Log.e(TAG, "文件不存在： $path")
//        val pictureRequestBody = picture.asRequestBody("image/*".toMediaTypeOrNull())
        val pictureRequestBody = picture.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val picturePart = MultipartBody.Part.createFormData("picture", picture.name, pictureRequestBody)

        val request = MyNetwork().getHttpService(RestorationRequest::class.java)

        Log.e(TAG, imageRequestParams["modelName"] + imageRequestParams["modelScale"] + ":" + modelId.toString())

        val callback = request.runModelOnPicture(userId, modelId, picturePart)
        callback.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                val res = response.body()
                if (res == null) {
                    restoreRequestStatus.postValue(0)
                    return
                }
                val code = res["code"]!!.toInt()
                restoreRequestStatus.postValue(code)
                var url = res["resultUrl"]
                if (url != null) {
                    imagesUrl["resultUrl"] = url
                }
                url = res["originUrl"]
                if (url != null) {
                    imagesUrl["originUrl"] = url
                }
                Log.e(TAG, "code: $code, res: $imagesUrl")
            }
            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e(TAG, "connect server failed!!")
            }
        })
    }

    fun isPictureOK(context: Context): Boolean {
        Common().getBitmapFromUrl(TAG, context, imagesUrl["resultUrl"]!!.replace(".", "_finish."))
        imgSR = imagesUrl["resultUrl"]?.let { Common().getBitmapFromUrl(TAG, context, it) }
        Common().getBitmapFromUrl(TAG, context, imagesUrl["originUrl"]!!.replace(".", "_finish."))
        imgLR = imagesUrl["originUrl"]?.let { Common().getBitmapFromUrl(TAG, context, it) }
        try {
            imgSR?.height
            imgLR?.height
            imageRequestCode.postValue(true)
            return true
        }catch (e: Exception){
            imageRequestCode.postValue(false)
            Log.e(TAG, "图片不存在!!")
        }
        return false
    }


    fun getImgSR(): Bitmap? { return imgSR }
    fun getImgLR(): Bitmap? { return imgLR }

    fun getNotify(): MutableLiveData<FragmentNotify> { return notify }
    fun setNotify(value: FragmentNotify) { notify.value = value }

    fun getImageRequestParams(): HashMap<String, String> { return imageRequestParams }
    fun getRestoreRequestStatus(): MutableLiveData<Int> { return restoreRequestStatus }
    fun getImageRequestCode(): MutableLiveData<Boolean> { return imageRequestCode }
}
