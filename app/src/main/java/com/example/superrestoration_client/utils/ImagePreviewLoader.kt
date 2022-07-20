package com.example.superrestoration_client.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.previewlibrary.loader.IZoomMediaLoader
import com.previewlibrary.loader.MySimpleTarget

class ImagePreviewLoader: IZoomMediaLoader {
    override fun displayImage(p0: Fragment, p1: String, p2: ImageView?, p3: MySimpleTarget) {
        GlideApp.with(p0).asBitmap().load(p1).apply(RequestOptions().fitCenter())
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    p3.onResourceReady()
                    p2!!.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun displayGifImage(p0: Fragment, p1: String, p2: ImageView?, p3: MySimpleTarget) {
        TODO("Not yet implemented")
    }

    override fun onStop(p0: Fragment) {
        GlideApp.with(p0).onStop()
    }

    override fun clearMemory(c: Context) {
        GlideApp.get(c).clearMemory();
    }
}