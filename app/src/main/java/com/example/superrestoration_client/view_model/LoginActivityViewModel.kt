package com.example.superrestoration_client.view_model

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.network.UserRequest
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.LiveDataManager
import com.example.superrestoration_client.utils.SharePreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivityViewModel: ViewModel() {
    private var user: MutableLiveData<User> = MutableLiveData()
    private var loginStatus: MutableLiveData<Int>  = MutableLiveData()
    init {
        user.value = User()
        loginStatus.value = -1
    }
    fun getUser(): MutableLiveData<User> {
        return this.user
    }

    fun setUser(value: User){
        this.user = LiveDataManager.with("user")
        this.user.postValue(value)
    }

    fun getLoginStatus(): MutableLiveData<Int> {
        return this.loginStatus
    }

    fun loadData(context: Context){
        // 读取上一次的登录信息
        val newUser = User()
        newUser.setUserName(SharePreferenceUtil.getData("loginfo", context, "UserName", String()) as String)
        newUser.setUserPwd(SharePreferenceUtil.getData("loginfo", context, "UserPwd", String()) as String)
        setUser(newUser)
    }

    fun loginRequest(){
        //请求服务器
        val userName = user.value!!.getUserName()
        val userPassword = user.value!!.getUserPwd()

        val retrofit = Retrofit.Builder().baseUrl(Config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())// 自动转换
            .build()
        val httpBinService = retrofit.create(UserRequest::class.java)
        val call: Call<Int> = httpBinService.userLogin(userName, userPassword)
        call.enqueue(object : Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                loginStatus.postValue(response.body())
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                loginStatus.postValue(0)
                Log.e(TAG, "login Failed!!")
            }
        })
    }
}