package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class Combination: BaseObservable() {
    private var combinationId: Int? = -1
    private var combinationName: String = ""
    private var combinationList: String = ""
    private var combinationOwnerId = -1
    private var combinationType = true

    @Bindable
    fun getName(): String { return combinationName }
    fun setName(value: String) { combinationName = value }

    fun getcombinationList(): ArrayList<Int> { return Gson().fromJson(combinationList, object: TypeToken<ArrayList<Int>>(){}.type) }
    fun setcombinationList(value: ArrayList<Int>) { combinationList = Gson().toJson(value) }

    fun getOwnerId(): Int { return combinationOwnerId }
    fun setOwnerId(value: Int) { combinationOwnerId = value }
    fun getType(): Boolean { return combinationType }
    fun setType(value: Boolean) { combinationType = value }
    fun getId(): Int? { return combinationId }
    fun setId(value: Int) { combinationId = value }

    override fun toString(): String {
        return "combinationId: $combinationId, combinationName:$combinationName,\n" +
                "combinationList:$combinationList,\n" +
                "combinationOwnerId:$combinationOwnerId,\n" +
                "combinationType:$combinationType。\n"
    }

}