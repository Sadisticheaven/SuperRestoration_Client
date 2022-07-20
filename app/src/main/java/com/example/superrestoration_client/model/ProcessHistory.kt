package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class ProcessHistory(): BaseObservable(){
    private var historyId: Int = -1
    private var userId: Int = -1
    private var modelsId: String = ""
    private var datasetsId: String = ""
    private var commitTime: String = ""
    constructor(id: Int, user: Int, models: String, datasets: String, time: String) : this() {
        historyId = id
        userId = user
        modelsId = models
        datasetsId = datasets
        commitTime = time
    }
    @Bindable
    fun getHistoryId(): Int {
        return historyId
    }

    @Bindable
    fun getUserId(): Int {
        return userId
    }

    @Bindable
    fun getModelsId(): String {
        return modelsId
    }

    @Bindable
    fun getDatasetsId(): String {
        return datasetsId
    }

    @Bindable
    fun getCommitTime(): String {
        return commitTime
    }

    override fun toString(): String {
        return "commitTime: $commitTime\n"
    }
}