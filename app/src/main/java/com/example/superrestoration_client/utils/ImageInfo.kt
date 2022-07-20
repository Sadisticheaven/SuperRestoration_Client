package com.example.superrestoration_client.utils

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import com.previewlibrary.enitity.IThumbViewInfo

class ImageInfo() : IThumbViewInfo {
    private var imageUrl: String = ""
    private var bounds: Rect = Rect()

    constructor(url: String) : this() {
        imageUrl = url
    }

    constructor(parcel: Parcel) : this() {
        imageUrl = parcel.readString().toString()
        bounds = parcel.readParcelable(Rect::class.java.classLoader)!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(this.imageUrl)
        p0?.writeParcelable(this.bounds, p1)
    }

    override fun getUrl(): String {
        return imageUrl
    }

    fun setUrl(value: String){
        imageUrl = value
    }

    override fun getBounds(): Rect {
        return bounds
    }

    fun setBounds(value: Rect){
        bounds = value
    }

    override fun getVideoUrl(): String? {
        return null
    }

    companion object CREATOR : Parcelable.Creator<ImageInfo> {
        override fun createFromParcel(parcel: Parcel): ImageInfo {
            return ImageInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageInfo?> {
            return arrayOfNulls(size)
        }
    }
}