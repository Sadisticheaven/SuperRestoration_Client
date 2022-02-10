package com.example.superrestoration_client.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 通过 SharedPreferences 保存用户信息
 * 保存路径为： /data/data/<package name>/shared_prefs
 */
object SharePreferenceUtil {
    fun writeData(fileName: String, context: Context, key: String, value: Any) {
        val dataType = value.javaClass.simpleName
        val editor: SharedPreferences.Editor =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
        when (dataType) {
            "Integer" -> {
                editor.putInt(key, value as Int)
            }
            "String" -> {
                editor.putString(key, value as String)
            }
            "Boolean" -> {
                editor.putBoolean(key, value as Boolean)
            }
            "Float" -> {
                editor.putFloat(key, value as Float)
            }
            "Long" -> {
                editor.putLong(key, value as Long)
            }
        }
        editor.apply()
    }

    fun getData(fileName: String, context: Context, key: String?, value: Any): Any? {
        var `object`: Any? = null
        val dataType = value.javaClass.simpleName
        val sp: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        when (dataType) {
            "Integer" -> {
                `object` = sp.getInt(key, value as Int)
            }
            "String" -> {
                `object` = sp.getString(key, value as String)
            }
            "Boolean" -> {
                `object` = sp.getBoolean(key, value as Boolean)
            }
            "Float" -> {
                `object` = sp.getFloat(key, value as Float)
            }
            "Long" -> {
                `object` = sp.getLong(key, value as Long)
            }
        }
        return `object`
    }
}