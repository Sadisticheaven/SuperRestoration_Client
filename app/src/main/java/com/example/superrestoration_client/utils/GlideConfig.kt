package com.example.superrestoration_client.utils

import android.content.Context
import android.view.RoundedCorner
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideConfig: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val maxSize = Runtime.getRuntime().maxMemory()
        builder.setMemoryCache(LruResourceCache(maxSize / 8))
        super.applyOptions(context, builder)
    }
}