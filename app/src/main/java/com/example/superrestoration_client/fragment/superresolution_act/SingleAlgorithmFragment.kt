package com.example.superrestoration_client.fragment.superresolution_act

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentSingleAlgorithmBinding
import com.example.superrestoration_client.fragment.LoadingDialog
import com.example.superrestoration_client.utils.ThreadManager
import com.example.superrestoration_client.view_model.SingleAlgorithmViewModel
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.snackbar.Snackbar

class SingleAlgorithmFragment : Fragment() {
    private val TAG = "SingleAlgorithmFragment"
    private lateinit var fragmentSingleAlgorithmBinding: FragmentSingleAlgorithmBinding
    private lateinit var singleAlgorithmViewModel: SingleAlgorithmViewModel
    private val superRestorationActivityViewModel:SuperRestorationActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSingleAlgorithmBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_single_algorithm, container, false)
        singleAlgorithmViewModel = ViewModelProvider(this)[SingleAlgorithmViewModel::class.java]
        initObserver()
        return fragmentSingleAlgorithmBinding.root
    }

    private fun initObserver() {
        superRestorationActivityViewModel.getCurrentImage().observe(viewLifecycleOwner){
            fragmentSingleAlgorithmBinding.displayImageSingle.setImageURI(it, null)
        }

        superRestorationActivityViewModel.getModelRequestStatus().observe(viewLifecycleOwner){
            if (it > 0) {
                initNumberPicker()
            } else if (it == 0) {
                Snackbar.make(
                    requireActivity().findViewById(R.id.snackBar_superRestoration),
                    "无法连接服务器！！",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        singleAlgorithmViewModel.getRestoreRequestStatus().observe(viewLifecycleOwner){
            when(it){
                -1 -> {}
                200 -> {
                    val dialog = LoadingDialog()
                    dialog.show(childFragmentManager, "loading")
                    ThreadManager.getFixedThreadPool().execute {
                        if(singleAlgorithmViewModel.isPictureOK(requireContext())) {
                            dialog.dismiss()
                        }
                        else Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                            "获取失败！", Snackbar.LENGTH_LONG).show()
                    }
                }
                else -> Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                    "请重试！", Snackbar.LENGTH_LONG).show()
            }
        }

        singleAlgorithmViewModel.getImageRequestCode().observe(viewLifecycleOwner){
            if (it){
                fragmentSingleAlgorithmBinding.displayImageSingle.setImageBitmap(
                    singleAlgorithmViewModel.getImgLR(),
                    singleAlgorithmViewModel.getImgSR())
            }
        }

        fragmentSingleAlgorithmBinding.seekBarCompare.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                fragmentSingleAlgorithmBinding.displayImageSingle.updateWidthRate(p1 / 100F)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun initNumberPicker() {
        updateModelNamePicker()
        fragmentSingleAlgorithmBinding.numberPickerModelName.setOnValueChangedListener { numberPicker, _, p2 ->
            val modelName = numberPicker.displayedValues[p2]
            singleAlgorithmViewModel.getImageRequestParams()["modelName"] = modelName
            updateScalePicker(modelName)
            Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                modelName, Snackbar.LENGTH_LONG).show()
        }
        fragmentSingleAlgorithmBinding.numberPickerModelScale.setOnValueChangedListener { numberPicker, _, p2 ->
            val modelScale = numberPicker.displayedValues[p2]
            singleAlgorithmViewModel.getImageRequestParams()["modelScale"] = modelScale
            Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                modelScale, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun updateModelNamePicker() {
        val modelsName = superRestorationActivityViewModel.getModelsMap().keys.toTypedArray()
        fragmentSingleAlgorithmBinding.numberPickerModelName.displayedValues = null
        fragmentSingleAlgorithmBinding.numberPickerModelName.minValue = 0
        fragmentSingleAlgorithmBinding.numberPickerModelName.maxValue = modelsName.size - 1
        fragmentSingleAlgorithmBinding.numberPickerModelName.value = 0
        fragmentSingleAlgorithmBinding.numberPickerModelName.displayedValues = modelsName
        singleAlgorithmViewModel.getImageRequestParams()["modelName"] = modelsName[0]
        updateScalePicker(modelsName[0])
    }

    private fun updateScalePicker(modelName: String) {
        val modelScale = superRestorationActivityViewModel.getModelsMap()[modelName]!!.keys.toTypedArray()
        fragmentSingleAlgorithmBinding.numberPickerModelScale.displayedValues = null
        fragmentSingleAlgorithmBinding.numberPickerModelScale.minValue = 0
        fragmentSingleAlgorithmBinding.numberPickerModelScale.maxValue = modelScale.size - 1
        fragmentSingleAlgorithmBinding.numberPickerModelScale.value = 0
        fragmentSingleAlgorithmBinding.numberPickerModelScale.displayedValues = modelScale
        singleAlgorithmViewModel.getImageRequestParams()["modelScale"] = modelScale[0]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSingleAlgorithmBinding.btnRunModelOnPicture.setOnClickListener {
            singleAlgorithmViewModel.runModelOnPicture(requireContext(),
                superRestorationActivityViewModel.getCurrentUser()!!,
                superRestorationActivityViewModel.getCurrentImage().value,
                superRestorationActivityViewModel.getModelsMap())
        }
    }
}