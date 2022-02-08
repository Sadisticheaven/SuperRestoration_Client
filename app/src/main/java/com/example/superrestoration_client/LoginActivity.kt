package com.example.superrestoration_client

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.superrestoration_client.databinding.ActivityLoginBinding
import com.example.superrestoration_client.view_model.LoginActivityViewModel
import com.example.superrestoration_client.utils.Common
import com.example.superrestoration_client.utils.SharePreferenceUtil
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var loginActivityViewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = Common().initViewModelProvider(application, this)
        loginActivityViewModel = viewModelProvider.get(LoginActivityViewModel::class.java)
        loginActivityViewModel.loadData(this)
        dataBinding()
        initObserver()
    }

    private fun dataBinding() {
        val activityLoginBinding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding.apply {
            viewModel = loginActivityViewModel
            lifecycleOwner = this@LoginActivity
        }
    }

    fun initObserver(){
        loginActivityViewModel.getLoginStatus().observe(this, Observer {
            if(it == 3){
                // 登录成功，保存登录信息并跳转到主界面
                SharePreferenceUtil.writeData(this, "UserName", loginActivityViewModel.getUser().value!!.getUserName())
                SharePreferenceUtil.writeData(this, "UserPwd", loginActivityViewModel.getUser().value!!.getUserPwd())
                var intent = Intent()
                intent.setClass(this, MainActivity::class.java)
                startActivity(intent)
                Log.e(ContentValues.TAG, "login status: $it")
            }else if (it > 0){
//                Common().alert(this, "用户名或密码错误！！")
//                Toast.makeText(this, "用户名或密码错误！！", Toast.LENGTH_LONG).show()
                Snackbar.make(window.decorView.findViewById(R.id.test_login), "用户名或密码错误！！", Snackbar.LENGTH_LONG).show()
            }else if (it == 0){
                Snackbar.make(window.decorView.findViewById(R.id.test_login), "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    fun test_login_Click(view: android.view.View) {
        loginActivityViewModel.loginRequest()
    }

}