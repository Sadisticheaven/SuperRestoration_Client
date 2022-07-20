package com.example.superrestoration_client.fragment.main_act

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentResultBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.utils.HistoryAdaptor
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar


class ResultFragment : Fragment() {
    private val TAG = "ResultFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerAdaptor: HistoryAdaptor
    private lateinit var fragmentResultBinding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentResultBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_result, container, false)
        recyclerView = fragmentResultBinding.recyclerHistories
        refreshLayout = fragmentResultBinding.refreshHistory
        imageRestorationViewModel.refreshHistory(shareViewModel.getCurrentUser().value!!.getUserId())
        initObserver()
        return fragmentResultBinding.root
    }

    private fun initObserver() {
        refreshLayout.setOnRefreshListener {
            imageRestorationViewModel.refreshHistory(shareViewModel.getCurrentUser().value!!.getUserId())
            if (refreshLayout.isRefreshing)
                refreshLayout.isRefreshing = false
        }
        imageRestorationViewModel.getHistoryRequestCode().observe(viewLifecycleOwner){
            if (it == 0)
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
            else if (it == 1) {
                updateItems()
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "刷新成功", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateItems(){
        recyclerAdaptor = HistoryAdaptor(
            imageRestorationViewModel.getHistory().value!!, this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: HistoryAdaptor.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                imageRestorationViewModel.getSelectedHistory().value =
                    imageRestorationViewModel.getHistory().value!![position]
                imageRestorationViewModel.loadModelsAndDatasetsInHistory(
                    shareViewModel.getModelList().value!!, shareViewModel.getDatasetList().value!!)
                imageRestorationViewModel.getImagesUrl().value = arrayListOf()
                shareViewModel.getNotify().postValue(
                    FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["PictureFragment"]!!))
            }
        })
        recyclerView.adapter = recyclerAdaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        refreshLayout.refreshDrawableState()
        refreshLayout.post {
            // 使用SwipeRefreshLayout更新了adapter后还要手动刷新SwipeRefreshLayout才能显示
            refreshLayout.isRefreshing = true
            refreshLayout.isRefreshing = false
        }
//        childFragmentManager.executePendingTransactions()
    }
}