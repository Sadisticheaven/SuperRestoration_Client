package com.example.superrestoration_client.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.superrestoration_client.R

class Common {
    fun initViewModelProvider(application: Application, owner: ViewModelStoreOwner): ViewModelProvider {
        // ActivityLoginBinding是apt自动生成的类，名字与activity_login.xml对应
        // 通过viewModelProvider避免onCreate时重新创建ViewModle导致数据源丢失
        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        return ViewModelProvider(owner, viewModelFactory)
    }

    fun alert(ctx: Context, msg: String) {
        var dialog = AlertDialog.Builder(ctx).setTitle("提示:")
            .setMessage(msg).setIcon(R.mipmap.ic_launcher_round).setCancelable(true)
            .create()
        dialog.show()
    }
}