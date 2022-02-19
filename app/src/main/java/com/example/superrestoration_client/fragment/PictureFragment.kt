package com.example.superrestoration_client.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentPictureBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar

class PictureFragment : Fragment() {
    private val TAG = "PictureFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentPictureBinding: FragmentPictureBinding
    private val models = arrayOf("srcnn", "fsrcnn", "srgan")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPictureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture, container, false)
        fragmentPictureBinding.modelPicker.displayedValues = models
        fragmentPictureBinding.modelPicker.minValue = 0
        fragmentPictureBinding.modelPicker.maxValue = models.size - 1
        initObserver()
        return fragmentPictureBinding.root
    }

    private fun initObserver() {
        fragmentPictureBinding.modelPicker.setOnValueChangedListener { p0, p1, p2 ->
            Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                models[p1], Snackbar.LENGTH_LONG).show()
        }
        fragmentPictureBinding.modelPicker.setOnScrollListener { p0, p1 ->  }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPictureBinding.btnBack.setOnClickListener{
            shareViewModel.getNotify().postValue(FragmentNotify(Config.fragmentTag[TAG]!!,
                Config.fragmentTag["ResultFragment"]!!))
        }
    }

}