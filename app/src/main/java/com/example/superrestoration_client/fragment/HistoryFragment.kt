package com.example.superrestoration_client.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentHistoryBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.HistoryAdaptor
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel


class HistoryFragment : Fragment() {
    private val TAG = "HistoryFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentHistoryBinding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        fragmentHistoryBinding.vpHistory
        initView()
        initObserver()
        return fragmentHistoryBinding.root
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner){
            when(it.to){
                Config.fragmentTag["ResultFragment"] -> fragmentHistoryBinding.vpHistory.currentItem = 0
                Config.fragmentTag["PictureFragment"] -> fragmentHistoryBinding.vpHistory.currentItem = 1
            }
        }
    }

    private fun initView() {
        val childFragments = arrayListOf(ResultFragment(), PictureFragment())
        val vpAdaptor = ViewPagerAdaptor(childFragments, requireActivity().supportFragmentManager, lifecycle)
        fragmentHistoryBinding.vpHistory.adapter = vpAdaptor
    }
}