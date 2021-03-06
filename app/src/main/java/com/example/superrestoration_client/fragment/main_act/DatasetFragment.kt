package com.example.superrestoration_client.fragment.main_act

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentDatasetBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.DatasetAdaptor
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.view_model.DatasetFragmentViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_dataset.*

class DatasetFragment:Fragment() {
    private val TAG = "DatasetFragment"
    private lateinit var datasetFragmentViewModel: DatasetFragmentViewModel
    private lateinit var fragmentDatasetBinding: FragmentDatasetBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptor: DatasetAdaptor
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentDatasetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dataset, container, false)
        datasetFragmentViewModel = ViewModelProvider(this)[DatasetFragmentViewModel::class.java]
        datasetFragmentViewModel.loadDatasets(shareViewModel)
        recyclerView = fragmentDatasetBinding.datasetsRecycler
        initObserver()
        return fragmentDatasetBinding.root
    }

    private fun initObserver(){
        datasetFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner) {
            if (it > 0) {
                updateItems()
            } else if (it == 0) {
                Snackbar.make(
                    requireActivity().findViewById(R.id.snackbar_container),
                    "???????????????????????????",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        shareViewModel.getNotify().observe(viewLifecycleOwner){
            if (it.to == Config.fragmentTag[TAG]){
                when(it.from) {
                    Config.fragmentTag["DatasetSelectedFragment"] -> fromDatasetSelectedFragment()
                    else ->  switchItemSelectable(false) // ???????????????????????????
                }
            }
        }
    }

    private fun updateItems(){
        adaptor = DatasetAdaptor(shareViewModel.getDatasetList().value!!, this.requireContext())
        // ??????Item??????????????????
        adaptor.setOnItemClickListener(object: DatasetAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (shareViewModel.addDataset(position)){
                    switchSelectButtonStatus(position, true)
                    println(shareViewModel.getSelectedDatasetsIndex())
                }else{
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container), "??????$position ????????????", Snackbar.LENGTH_LONG).show()
                }
            }
            override fun onRemoveButtonClick(view: View, position: Int) {
                if (shareViewModel.removeDataset(position)){
                    switchSelectButtonStatus(position, false)
                    println(shareViewModel.getSelectedDatasetsIndex())
                }else{
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container), "??????$position ????????????", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
    }

    fun switchSelectButtonStatus(position: Int, selected: Boolean){
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

    private fun switchItemSelectable(selectable: Boolean){
        if (selectable)
            adaptor.setItemVisibility(DatasetAdaptor.ItemVisibility(View.VISIBLE, View.GONE))
        else
            adaptor.setItemVisibility(DatasetAdaptor.ItemVisibility(View.GONE, View.GONE))
        recyclerView.adapter = adaptor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_dataset_finish.setOnClickListener{
            when(shareViewModel.getNotify().value!!.from){
                Config.fragmentTag["DatasetSelectedFragment"] -> backToDatasetSelectedFragment()
            }
        }
    }

    private fun fromDatasetSelectedFragment() {
        fragmentDatasetBinding.addDatasetFinish.visibility = View.VISIBLE
        switchItemSelectable(true)
        for (idx in shareViewModel.getSelectedDatasetsIndex())
            switchSelectButtonStatus(idx, true)
    }

    private fun backToDatasetSelectedFragment() {
        fragmentDatasetBinding.addDatasetFinish.visibility = View.GONE
        switchItemSelectable(false)
        shareViewModel.getNotify().postValue(
            FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["DatasetSelectedFragment"]!!)
        )
    }
}