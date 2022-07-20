package com.example.superrestoration_client

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.superrestoration_client.databinding.ActivitySuperResolutionBinding
import com.example.superrestoration_client.fragment.superresolution_act.CompareFragment
import com.example.superrestoration_client.fragment.superresolution_act.MultiAlgorithmFragment
import com.example.superrestoration_client.fragment.superresolution_act.SelectFragment
import com.example.superrestoration_client.fragment.superresolution_act.SingleAlgorithmFragment
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.tabs.TabLayoutMediator

class SuperResolutionActivity : AppCompatActivity() {
    private val TAG = "SuperResolutionActivity"
    private lateinit var activitySuperResolutionBinding: ActivitySuperResolutionBinding
    private lateinit var superRestorationActivityViewModel: SuperRestorationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySuperResolutionBinding = DataBindingUtil.setContentView(this, R.layout.activity_super_resolution)
        superRestorationActivityViewModel = ViewModelProvider(this)[SuperRestorationActivityViewModel::class.java]
        superRestorationActivityViewModel.setCurrentUser(intent.getParcelableExtra("currentUser")!!)
        superRestorationActivityViewModel.loadModels()
        initView()
        initObserver()
    }

    private fun initView() {
        val childFragments = arrayListOf(
            SelectFragment(), CompareFragment()
        )
        val vpAdaptor = ViewPagerAdaptor(childFragments, supportFragmentManager, lifecycle)
        activitySuperResolutionBinding.vpSuperResolutionWrapper.adapter = vpAdaptor
        activitySuperResolutionBinding.vpSuperResolutionWrapper.isUserInputEnabled = false
    }

    fun initObserver(){
        superRestorationActivityViewModel.getNotify().observe(this){
            when(it.to){
                Config.fragmentTag["CompareFragment"] -> {
                    activitySuperResolutionBinding.vpSuperResolutionWrapper.currentItem = 1
                }
                Config.fragmentTag["MultiAlgorithmFragment"] -> {
                    activitySuperResolutionBinding.vpSuperResolutionWrapper.currentItem = 0
                }
                Config.fragmentTag["SingleAlgorithmFragment"] -> {
                    activitySuperResolutionBinding.vpSuperResolutionWrapper.currentItem = 0
                }
            }
        }
    }
}