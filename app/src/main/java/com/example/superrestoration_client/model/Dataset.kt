package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class Dataset: BaseObservable() {
    private var datasetId = 0
    private var datasetName: String = ""
    private var datasetSavePath: String = ""
    private var datasetOwnerId = 0
    private var datasetSize = 0
    private var datasetAltTime: String = ""

    override fun toString(): String {
        return "datasetId: $datasetId, datasetName:$datasetName,\n" +
                "datasetSavePath:$datasetSavePath,\n" +
                "datasetOwnerId:$datasetOwnerId,\n" +
                "datasetSize:$datasetSize,\n" +
                "datasetAltTime:$datasetAltTime。\n"
    }

    @Bindable
    fun getDatasetId(): Int { return datasetId }

    fun setDatasetId(value: Int){ datasetId = value }

    @Bindable
    fun getDatasetName(): String { return datasetName }

    fun setDatasetName(value: String){ datasetName = value }

    @Bindable
    fun getDatasetSavePath(): String { return datasetSavePath }

    fun setDatasetSavePath(value: String){ datasetSavePath = value }

    @Bindable
    fun getDatasetOwnerId(): Int { return datasetOwnerId }


    @Bindable
    fun getDatasetSize(): Int { return datasetSize }

    @Bindable
    fun getDatasetAltTime(): String { return datasetAltTime }
}