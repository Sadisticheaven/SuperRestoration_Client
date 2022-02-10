package com.example.superrestoration_client.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.superrestoration_client.model.Combination
import com.example.superrestoration_client.model.Dataset
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.User
import com.example.superrestoration_client.utils.SharePreferenceUtil

class MainActivityShareViewModel : ViewModel() {
    private var modelList: MutableLiveData<ArrayList<Model>> = MutableLiveData()
    private var selectedModels: MutableLiveData<MutableSet<Int>> = MutableLiveData()

    private var datasetList: MutableLiveData<ArrayList<Dataset>> = MutableLiveData()
    private var selectedDatasets: MutableLiveData<MutableSet<Int>> = MutableLiveData()

    private var combinationList: MutableLiveData<HashMap<String, Combination>> = MutableLiveData()

    init {
        modelList.value = ArrayList()
        selectedModels.value = mutableSetOf()
        datasetList.value = ArrayList()
        selectedDatasets.value = mutableSetOf()
        combinationList.value = hashMapOf()
    }

    fun addModel(position: Int): Boolean { return selectedModels.value!!.add(position) }
    fun addDataset(position: Int): Boolean { return selectedDatasets.value!!.add(position) }

    fun removeModel(position: Int): Boolean { return selectedModels.value!!.remove(position) }
    fun removeDataset(position: Int): Boolean { return selectedDatasets.value!!.remove(position) }

    fun getSelectedModels(): MutableSet<Int> { return selectedModels.value!! }
    fun getSelectedDatasets(): MutableSet<Int> { return selectedDatasets.value!! }

    fun getModelList(): MutableLiveData<ArrayList<Model>> { return modelList }
    fun getDatasetList(): MutableLiveData<ArrayList<Dataset>> { return datasetList }
    fun addNewCombination(newCombination: Combination) { combinationList.value!![newCombination.getName()] = newCombination }
    fun getCombinations(): HashMap<String, Combination> { return combinationList.value!! }
}