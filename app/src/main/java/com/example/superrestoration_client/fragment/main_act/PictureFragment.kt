package com.example.superrestoration_client.fragment.main_act

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentPictureBinding
import com.example.superrestoration_client.utils.*
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import com.previewlibrary.GPreviewBuilder
import com.previewlibrary.ZoomMediaLoader

class PictureFragment : Fragment() {
    private val TAG = "PictureFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentPictureBinding: FragmentPictureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPictureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture, container, false)
        updateItems()
        initObserver()
        return fragmentPictureBinding.root
    }

    private fun updateItems() {
        ZoomMediaLoader.getInstance().init(ImagePreviewLoader())
        val images = arrayListOf<ImageInfo>()
        for (url in imageRestorationViewModel.getImagesUrl().value!!)
            images.add(ImageInfo(Config.baseUrl + "/result/" + url))
        val adaptor = ImageAdaptor(imageRestorationViewModel.getImagesUrl().value!!, this.requireContext())
        adaptor.setOnItemClickListener(object: ImageAdaptor.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                GPreviewBuilder.from(this@PictureFragment).setData(images)
                    .setCurrentIndex(position).setSingleFling(true).setDrag(true)
                    .setType(GPreviewBuilder.IndicatorType.Number)
                    .start()
            }
        })
        fragmentPictureBinding.recyclerImages.adapter =adaptor

        fragmentPictureBinding.recyclerImages.layoutManager = StaggeredGridLayoutManager(3,
            StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner){
            when(it.to){
                Config.fragmentTag[TAG] -> {
                    initNumberPicker()
                    updateItems()
                }
            }
        }
        imageRestorationViewModel.getImagesUrl().observe(viewLifecycleOwner){
            Log.e(TAG, it.toString())
            updateItems()
        }
    }

    private fun initNumberPicker() {
        val modelMap = hashMapOf<String, Int>()
        for (model in imageRestorationViewModel.getModelsInHistory().value?:return)
            modelMap[model.getModelName() + " x${model.getModelScale()}"] = model.getModelId()
        val modelsName = modelMap.keys.toTypedArray()

        val datasetMap = hashMapOf<String, Int>()
        for (dataset in imageRestorationViewModel.getDatasetsInHistory().value?:return)
            datasetMap[dataset.getDatasetName()] = dataset.getDatasetId()
        val datasetsName = datasetMap.keys.toTypedArray()

        imageRestorationViewModel.getImageRequestParams()["modelId"] = modelMap[modelsName[0]]!!
        imageRestorationViewModel.getImageRequestParams()["datasetId"] = datasetMap[datasetsName[0]]!!
        // 先清空displayedValues防止数组越界
        fragmentPictureBinding.modelPicker.displayedValues = null
        fragmentPictureBinding.modelPicker.maxValue = modelsName.size - 1
        fragmentPictureBinding.modelPicker.minValue = 0
        fragmentPictureBinding.modelPicker.value = 0
        fragmentPictureBinding.modelPicker.displayedValues = modelsName
        fragmentPictureBinding.modelPicker.setOnValueChangedListener { _, _, p2 ->
            imageRestorationViewModel.getImageRequestParams()["modelId"] = modelMap[modelsName[p2]]!!
            Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                modelsName[p2], Snackbar.LENGTH_LONG).show()
        }
        fragmentPictureBinding.modelPicker.setOnScrollListener { _, _ ->  }

        fragmentPictureBinding.datasetPicker.displayedValues = null
        fragmentPictureBinding.datasetPicker.minValue = 0
        fragmentPictureBinding.datasetPicker.value = 0
        fragmentPictureBinding.datasetPicker.maxValue = datasetsName.size - 1
        fragmentPictureBinding.datasetPicker.displayedValues = datasetsName
        fragmentPictureBinding.datasetPicker.setOnValueChangedListener { _, _, p2 ->
            imageRestorationViewModel.getImageRequestParams()["datasetId"] = datasetMap[datasetsName[p2]]!!
            Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                datasetsName[p2], Snackbar.LENGTH_LONG).show()
        }
        fragmentPictureBinding.datasetPicker.setOnScrollListener { _, _ ->  }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPictureBinding.btnBack.setOnClickListener{
            shareViewModel.getNotify().postValue(FragmentNotify(Config.fragmentTag[TAG]!!,
                Config.fragmentTag["ResultFragment"]!!))
        }
        fragmentPictureBinding.btnSearch.setOnClickListener{
            Log.e(TAG, imageRestorationViewModel.getImageRequestParams().toString())
            imageRestorationViewModel.requestImagesUrl()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "resume")
    }
}