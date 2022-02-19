package com.example.superrestoration_client.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentResultBinding
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.model.ProcessHistory
import com.example.superrestoration_client.utils.HistoryAdaptor
import com.example.superrestoration_client.utils.ModelAdaptor
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
        fragmentResultBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
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
        recyclerAdaptor = HistoryAdaptor(imageRestorationViewModel.getHistory().value!!, this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: HistoryAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
            }
        })
        recyclerView.adapter = recyclerAdaptor
        // 设置布局，否则无法显示
        refreshLayout.refreshDrawableState()
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
        refreshLayout.post {
            // 使用SwipeRefreshLayout更新了adapter后还要手动刷新SwipeRefreshLayout才能显示
            refreshLayout.isRefreshing = true
            refreshLayout.isRefreshing = false
        }
    }
}