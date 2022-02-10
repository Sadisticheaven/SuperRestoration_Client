package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class Combination: BaseObservable() {
    private var name: String = "自定义组合"
    private var indexes: List<Int> = listOf()

    fun getIndexes(): List<Int> { return indexes }
    fun setIndexes(value: List<Int>) { indexes = value }

    @Bindable
    fun getName(): String { return name }

    fun setName(value: String) { name = value }
}