package com.example.superrestoration_client.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class Model: BaseObservable() {
    private var modelId = 0
    private var modelName: String = ""
    private var archPath: String = ""
    private var paramsPath: String = ""
    private var modelDescription: String = ""
    private var introPagePath: String = ""
    private var modelScale: Int = 1

    override fun toString(): String {
        return "modelId: $modelId, modelName:$modelName,\n" +
                "modelScale:$modelScale,\n" +
                "archPath:$archPath,\n" +
                "paramsPath:$paramsPath,\n" +
                "modelDescription:$modelDescription,\n" +
                "introPagePath:$introPagePath。\n"
    }

    @Bindable
    fun getModelId(): Int { return modelId }

    fun setModelId(value: Int){ modelId = value }

    @Bindable
    fun getModelName(): String { return modelName }

    fun setModelName(value: String){ modelName = value }

    @Bindable
    fun getModelScale(): Int { return modelScale }

    @Bindable
    fun getArchPath(): String { return archPath }

    fun setArchPath(value: String){ archPath = value }

    @Bindable
    fun getParamsPath(): String { return paramsPath }


    @Bindable
    fun getModelDescription(): String { return modelDescription }

    @Bindable
    fun getIntroPagePath(): String { return introPagePath }
}