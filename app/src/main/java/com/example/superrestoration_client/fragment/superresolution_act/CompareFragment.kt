package com.example.superrestoration_client.fragment.superresolution_act

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentCompareBinding
import com.example.superrestoration_client.fragment.LoadingDialog
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.utils.ThreadManager
import com.example.superrestoration_client.view_model.CompareFragmentViewModel
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.snackbar.Snackbar

class CompareFragment : Fragment() {
    private val TAG = "CompareFragment"
    private lateinit var fragmentCompareBinding: FragmentCompareBinding
    private lateinit var compareFragmentViewModel: CompareFragmentViewModel
    private val superRestorationActivityViewModel: SuperRestorationActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCompareBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare, container, false)
        compareFragmentViewModel = ViewModelProvider(this)[CompareFragmentViewModel::class.java]
        initObserver()
        return fragmentCompareBinding.root
    }

    fun initObserver(){
        superRestorationActivityViewModel.getNotify().observe(viewLifecycleOwner){
            if(it.to == Config.fragmentTag[TAG]) loadPictures()
        }
        compareFragmentViewModel.getImageRequestCode().observe(viewLifecycleOwner){
            if (it) initPicker()
        }

        fragmentCompareBinding.seekBarCompareFragment.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                fragmentCompareBinding.displayImage.updateWidthRate(p1 / 100F)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun initPicker() {
        updateModelNamePicker(fragmentCompareBinding.pickerModelName1)
        updateModelNamePicker(fragmentCompareBinding.pickerModelName2)
        fragmentCompareBinding.pickerModelName1.setOnValueChangedListener { numberPicker, _, p2 ->
            val modelName = numberPicker.displayedValues[p2]
            fragmentCompareBinding.displayImage.replaceLeftImageBitmap(
                compareFragmentViewModel.getImages()[modelName])
        }
        fragmentCompareBinding.pickerModelName2.setOnValueChangedListener { numberPicker, _, p2 ->
            val modelName = numberPicker.displayedValues[p2]
            fragmentCompareBinding.displayImage.replaceRightImageBitmap(
                compareFragmentViewModel.getImages()[modelName])
        }
        fragmentCompareBinding.displayImage.setImageBitmap(
            compareFragmentViewModel.getImages()[fragmentCompareBinding.pickerModelName1.displayedValues[0]],
            compareFragmentViewModel.getImages()[fragmentCompareBinding.pickerModelName2.displayedValues[0]])
    }

    private fun updateModelNamePicker(picker: NumberPicker) {
        val modelsName = superRestorationActivityViewModel.getSelectedModelsName()?.toTypedArray()
        picker.displayedValues = null
        picker.minValue = 0
        picker.maxValue = (modelsName?.size ?: 1) - 1
        picker.value = 0
        picker.displayedValues = modelsName
    }

    private fun loadPictures() {
        val dialog = LoadingDialog()
        dialog.show(childFragmentManager, "loading")
        ThreadManager.getFixedThreadPool().execute {
            if(compareFragmentViewModel.isPicturesOK(requireActivity().applicationContext,
                    superRestorationActivityViewModel.getSelectedModelsName()!!,
                    superRestorationActivityViewModel.getScaleSelected(),
                    superRestorationActivityViewModel.getImagesUrl()!!)) {
                dialog.dismiss()
            }
            else Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                "获取失败！", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCompareBinding.btnBackCompareFragment.setOnClickListener{
            superRestorationActivityViewModel.getNotify().postValue(
                FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["MultiAlgorithmFragment"]!!))
        }
    }
}