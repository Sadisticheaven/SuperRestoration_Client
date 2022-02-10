package com.example.superrestoration_client.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.superrestoration_client.R

class Common {
    fun alert(ctx: Context, msg: String) {
        val dialog = AlertDialog.Builder(ctx).setTitle("提示:")
            .setMessage(msg).setIcon(R.mipmap.ic_launcher_round).setCancelable(true)
            .create()
        dialog.show()
    }
}