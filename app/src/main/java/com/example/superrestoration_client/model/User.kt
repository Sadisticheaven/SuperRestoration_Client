package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.superrestoration_client.BR

class User: BaseObservable() {
    private var userId: Int = -1
    private var userPwd: String = ""
    private var userName: String = ""
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
}