package com.example.superrestoration_client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.utils.DatasetAdaptor
import com.example.superrestoration_client.view_model.DatasetFragmentViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar

class DatasetFragment:Fragment() {
    private lateinit var datasetFragmentViewModel: DatasetFragmentViewModel
    private lateinit var thisView: View
    private lateinit var recyclerView: RecyclerView
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_dataset, container, false)
        datasetFragmentViewModel = ViewModelProvider(this)[DatasetFragmentViewModel::class.java]
        datasetFragmentViewModel.loadDatasets(shareViewModel)
        recyclerView = thisView.findViewById(R.id.datasets_recycler) as RecyclerView
        updateItems()
        initObserver()
        return thisView
    }

    private fun initObserver(){
        datasetFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner) {
            if (it > 0) {
                updateItems()
            } else if (it == 0) {
                Snackbar.make(
                    thisView.findViewById(R.id.datasets_recycler),
                    "无法连接服务器！！",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateItems(){
        val adaptor = DatasetAdaptor(shareViewModel.getDatasetList().value!!, this.requireContext())
        // 实现Item中控件的回调
        adaptor.setOnItemClickListener(object: DatasetAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (shareViewModel.addDataset(position)){
                    switchButton(position, true)
                    println(shareViewModel.getSelectedDatasets())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.datasets_recycler), "添加$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                if (shareViewModel.removeDataset(position)){
                    switchButton(position, false)
                    println(shareViewModel.getSelectedDatasets())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.datasets_recycler), "移除$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        recyclerView.adapter = adaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
        // 由于Fragment切换时是异步commit，因此在回调中更改按钮状态
        recyclerView.viewTreeObserver.addOnDrawListener {
            for (idx in shareViewModel.getSelectedDatasets())
                switchButton(idx, true)
        }
    }

    fun switchButton(position: Int, selected: Boolean){
        val itemView =  recyclerView.layoutManager!!.findViewByPosition(position)
        val addButton: ImageButton = itemView!!.findViewById(R.id.add_dataset_to_list)
        val removeButton: ImageButton = itemView.findViewById(R.id.remove_dataset_from_list)
        if (selected){
            addButton.visibility = View.GONE
            removeButton.visibility = View.VISIBLE
        }else{
            addButton.visibility = View.VISIBLE
            removeButton.visibility = View.GONE
        }
    }
}