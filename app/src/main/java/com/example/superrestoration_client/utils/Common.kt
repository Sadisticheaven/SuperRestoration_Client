package com.example.superrestoration_client.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.superrestoration_client.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.security.MessageDigest

class Common {
    fun alert(ctx: Context, msg: String) {
        val dialog = AlertDialog.Builder(ctx).setTitle("提示:")
            .setMessage(msg).setIcon(R.mipmap.ic_launcher_round).setCancelable(true)
            .create()
        dialog.show()
    }

    fun catchBitmaps(left: Bitmap, right: Bitmap, factor: Float): Bitmap? {

        if (left.width == right.width && left.height == right.height){
            val width = left.width
            val height = left.height
            var res = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val leftPartWidth = (width * factor).toInt()
            var croppedLeft = Rect( 0, 0, leftPartWidth, height)
//        Bitmap.createBitmap(left, 0, 0, leftPartWidth, left.height)

            var croppedRight = Rect( leftPartWidth + 1, 0, width, height)
//        Bitmap.createBitmap(right, leftPartWidth + 1, 0, left.width - leftPartWidth, left.height)
            var canvas = Canvas(res)
            canvas.drawBitmap(left, croppedLeft, croppedLeft, null)
            canvas.drawBitmap(right, croppedRight, croppedRight, null)
            return res
        }else{
            Log.e("catchBitmaps", "bitmap1 and bitmap2 must be same size!!")
            return null
        }
    }


    fun getRequestPartFromUri(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        var path = Uri2Path.getFilePathByUri(context, uri)
        if (path == null){
            path = Uri2Path.getRealPathFromURI(context, uri)
        }
        val picture = File(path)
        if(!picture.exists())
            Log.e(ContentValues.TAG, "文件不存在： $path")
        val pictureRequestBody = picture.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, picture.name, pictureRequestBody)
    }

    fun getBitmapFromUrl(TAG: String, context: Context, url: String): Bitmap? {
        var flag = true
        var bitmap: Bitmap? = null
        while (flag){
            Log.e(TAG, "开始拉取图片： $url。")
            GlideApp.with(context).asBitmap()
                .load(Config.baseUrl + "/result/" + url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        flag = false
                        bitmap = resource
                        Log.e(TAG, "加载图片成功!!")
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.e(TAG, "加载取消或图片被释放。")
                    }
                })
            SystemClock.sleep(1000)
            Log.e(TAG, "休眠1s。")
        }

        try {
            bitmap?.height
            return bitmap
        }catch (e: Exception){
            Log.e(TAG, "图片不存在!!")
        }
        return null
    }

//    fun<T> ArrayList2MutableSet(arrayList: ArrayList<T>){
//        var mutableSet: MutableSet<T> = mutableSetOf()
//        for (element in mutableSet){
//            mutableSet.addAll(arrayList)
//        }
//        return  ArrayList<T>
//    }
}