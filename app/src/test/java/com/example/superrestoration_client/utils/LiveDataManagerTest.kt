package com.example.superrestoration_client.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Assert.*
import org.junit.Test

class LiveDataManagerTest{
    @Test
    fun withTest(){

    }

    @Test
    fun substract(){
        val set1 = mutableSetOf(1, 3 ,2)
        val set2 = mutableSetOf(1, 2)
        set1.remove(2)
        println(set1)
        println(set2)
        println(set1.subtract(set2))
    }
}