package com.example.superrestoration_client.utils

import androidx.lifecycle.MutableLiveData
import com.example.superrestoration_client.model.User
import java.util.*
import kotlin.collections.HashMap

/**
 * 管理Livedata的容器, 单例模式，因此用kotlin的object关键字
 */
object LiveDataManager {
    private var map = HashMap<String, MutableLiveData<Object>>()

    fun <T> with(key: String): MutableLiveData<T> {
        if (!map.containsKey(key)){
            map[key] = MutableLiveData<Object>()
        }
        return map[key] as MutableLiveData<T>
    }
}