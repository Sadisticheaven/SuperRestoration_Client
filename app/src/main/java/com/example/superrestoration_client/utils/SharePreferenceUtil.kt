package com.example.superrestoration_client.utils

import android.content.Context
import android.content.SharedPreferences

object SharePreferenceUtil {
    private const val FILE_NAME = "sp"
    fun writeData(context: Context, key: String, value: Any) {
        val dataType = value.javaClass.simpleName
        val editor: SharedPreferences.Editor =
            context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit()
        if ("Integer" == dataType) {
            editor.putInt(key, value as Int)
        } else if ("String" == dataType) {
            editor.putString(key, value as String)
        } else if ("Boolean" == dataType) {
            editor.putBoolean(key, value as Boolean)
        } else if ("Float" == dataType) {
            editor.putFloat(key, value as Float)
        } else if ("Long" == dataType) {
            editor.putLong(key, value as Long)
        }
        editor.apply()
    }

    fun getData(context: Context, key: String?, value: Any): Any? {
        var `object`: Any? = null
        val dataType = value.javaClass.simpleName
        val sp: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        if ("Integer" == dataType) {
            `object` = sp.getInt(key, value as Int)
        } else if ("String" == dataType) {
            `object` = sp.getString(key, value as String)
        } else if ("Boolean" == dataType) {
            `object` = sp.getBoolean(key, value as Boolean)
        } else if ("Float" == dataType) {
            `object` = sp.getFloat(key, value as Float)
        } else if ("Long" == dataType) {
            `object` = sp.getLong(key, value as Long)
        }
        return `object`
    }
}