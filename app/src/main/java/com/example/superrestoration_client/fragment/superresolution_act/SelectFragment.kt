package com.example.superrestoration_client.fragment.superresolution_act

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.ActivitySuperResolutionBinding
import com.example.superrestoration_client.databinding.FragmentSelectBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.tabs.TabLayoutMediator


class SelectFragment : Fragment() {
    private val TAG = "SelectFragment"
    private lateinit var fragmentSelectBinding: FragmentSelectBinding
    private val superRestorationActivityViewModel: SuperRestorationActivityViewModel by activityViewModels()

    private var activityLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        superRestorationActivityViewModel.getCurrentImage().postValue(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSelectBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_select, container, false)
        initView()
        initObserver()
        return fragmentSelectBinding.root
    }

    private fun initView() {
        val childFragments = arrayListOf(
            SingleAlgorithmFragment(), MultiAlgorithmFragment()
        )
        val vpAdaptor = ViewPagerAdaptor(childFragments, childFragmentManager, lifecycle)
        fragmentSelectBinding.vpSuperResolution.adapter = vpAdaptor
        fragmentSelectBinding.vpSuperResolution.isUserInputEnabled = false
        TabLayoutMediator(fragmentSelectBinding.navTabSuperResolution,
            fragmentSelectBinding.vpSuperResolution
        ) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.single_algorithm)
                1 -> tab.text = resources.getString(R.string.multi_algorithms)
            }
        }.attach()
    }

    fun initObserver(){
        superRestorationActivityViewModel.getNotify().observe(viewLifecycleOwner){
            when(it.to){
                Config.fragmentTag["MultiAlgorithmFragment"] -> {
                    fragmentSelectBinding.vpSuperResolution.currentItem = 1
                }
                Config.fragmentTag["SingleAlgorithmFragment"] -> {
                    fragmentSelectBinding.vpSuperResolution.currentItem = 0
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSelectBinding.btnSelectImage.setOnClickListener {
            activityLauncher.launch("image/*")
        }
    }
}