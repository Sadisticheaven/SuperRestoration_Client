package com.example.superrestoration_client.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentModelBinding
import com.example.superrestoration_client.databinding.FragmentModelCombinationBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 显示模型列表的界面
 */
class ModelCombinationFragment: Fragment() {
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private lateinit var fragmentModelCombinationBinding: FragmentModelCombinationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        thisView = inflater.inflate(R.layout.fragment_model_combination, container, false)
        fragmentModelCombinationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_model_combination, container, false)
        initView()
        initObserver()
        return fragmentModelCombinationBinding.root
    }

    private fun initView() {
//        val navHost = childFragmentManager.findFragmentById(R.id.navHost_modelCombination)
//        val navController = NavHostFragment.findNavController(navHost!!)
//        // 将navController与navBottom绑定
//        NavigationUI.setupWithNavController(fragmentModelCombinationBinding.navBottomModelCombination, navController)
        val childFragments = arrayListOf(
            ModelFragment(), CombinationFragment()
        )
        val vpAdaptor = ViewPagerAdaptor(childFragments, requireActivity().supportFragmentManager, lifecycle)
        fragmentModelCombinationBinding.vpModelCombination.adapter = vpAdaptor
        TabLayoutMediator(fragmentModelCombinationBinding.navTabModelCombination,
            fragmentModelCombinationBinding.vpModelCombination
        ) { tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.algorithm)
                1 -> tab.text = resources.getString(R.string.combination)
            }
        }.attach()
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner){
            when(it.to){
                Config.fragmentTag["ModelFragment"] -> fragmentModelCombinationBinding.vpModelCombination.currentItem = 0
                Config.fragmentTag["CombinationFragment"] -> fragmentModelCombinationBinding.vpModelCombination.currentItem = 1
            }
        }
    }
}