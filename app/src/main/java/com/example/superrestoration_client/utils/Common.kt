package com.example.superrestoration_client.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.superrestoration_client.R
import com.example.superrestoration_client.network.RestorationRequest
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Common {
    fun alert(ctx: Context, msg: String) {
        val dialog = AlertDialog.Builder(ctx).setTitle("提示:")
            .setMessage(msg).setIcon(R.mipmap.ic_launcher_round).setCancelable(true)
            .create()
        dialog.show()
    }

//    fun<T> ArrayList2MutableSet(arrayList: ArrayList<T>){
//        var mutableSet: MutableSet<T> = mutableSetOf()
//        for (element in mutableSet){
//            mutableSet.addAll(arrayList)
//        }
//        return  ArrayList<T>
//    }
}