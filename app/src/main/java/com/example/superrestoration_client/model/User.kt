package com.example.superrestoration_client.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.superrestoration_client.BR

class User() : BaseObservable(), Parcelable {
    private var userId: Int = -1
    private var userPwd: String = ""
    private var userName: String = ""

    // 此处的赋值顺序要与写入的顺序相同
    constructor(parcel: Parcel) : this() {
        userId = parcel.readInt()
        userName = parcel.readString()!!
        userPwd = parcel.readString()!!
    }

    @Bindable
    fun getUserId(): Int {
        return userId
    }

    fun setUserId(user_id: Int){
        this.userId = user_id
        notifyPropertyChanged(BR.userId)
    }
    @Bindable
    fun getUserName(): String {
        return userName
    }

    fun setUserName(user_name: String){
        this.userName = user_name
        notifyPropertyChanged(BR.userName)
    }

    @Bindable
    fun getUserPwd(): String {
        return userPwd
    }

    fun setUserPwd(userPwd: String){
        this.userPwd = userPwd
        notifyPropertyChanged(BR.userPwd)
    }

    override fun describeContents(): Int { return 0 }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(this.userId)
        p0?.writeString(this.userName)
        p0?.writeString(this.userPwd)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}