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
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentDatasetSelectedBinding
import com.example.superrestoration_client.model.Dataset
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.DatasetAdaptor
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_dataset_selected.*

class DatasetSelectedFragment : Fragment() {
    private val TAG = "DatasetSelectedFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentDatasetSelectedBinding: FragmentDatasetSelectedBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: DatasetAdaptor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentDatasetSelectedBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dataset_selected, container, false)
        recyclerView = fragmentDatasetSelectedBinding.rycSelectedDatasets
        initObserver()
        return fragmentDatasetSelectedBinding.root
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner) {
            if (it.to == Config.fragmentTag[TAG]
                && it.from == Config.fragmentTag["DatasetFragment"]){
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "刷新列表！！", Snackbar.LENGTH_LONG).show()
                var selectedDatasets = ArrayList<Dataset>()
                for (idx in shareViewModel.getSelectedDatasetsIndex())
                    selectedDatasets.add(shareViewModel.getDatasetList().value!![idx])
                var selectedDatasetsIndex = arrayListOf<Int>()
                selectedDatasetsIndex.addAll(shareViewModel.getSelectedDatasetsIndex())
                imageRestorationViewModel.getSelectedDatasetsIndex().value = selectedDatasetsIndex
                updateItems(selectedDatasets)
            }
        }
    }

    private fun updateItems(selectedDatasets: ArrayList<Dataset>){
        recyclerAdaptor = DatasetAdaptor(selectedDatasets, this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: DatasetAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {}

            override fun onRemoveButtonClick(view: View, position: Int) {
                recyclerAdaptor.removeItem(position)
                imageRestorationViewModel.getSelectedDatasetsIndex().value!!.removeAt(position)
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "移除$position 成功！！", Snackbar.LENGTH_LONG).show()
            }

        })
        recyclerAdaptor.setItemVisibility(DatasetAdaptor.ItemVisibility(View.GONE, View.VISIBLE))
        recyclerView.adapter = recyclerAdaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_datasets_or_combinations.setOnClickListener{
            var selectedDatasetsIndex = mutableSetOf<Int>()
            selectedDatasetsIndex.addAll(imageRestorationViewModel.getSelectedDatasetsIndex().value!!)
            shareViewModel.setSelectedDatasetsIndex(selectedDatasetsIndex)
            shareViewModel.getNotify().postValue(
                FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["DatasetFragment"]!!)
            )
        }
    }
}