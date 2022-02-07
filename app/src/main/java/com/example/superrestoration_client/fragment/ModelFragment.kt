package com.example.superrestoration_client.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.utils.Common
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.view_model.LoginActivityViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar

class ModelFragment:Fragment() {
    private lateinit var modelFragmentViewModel: ModelFragmentViewModel
    private lateinit var thisView: View
    private lateinit var viewModelProvider: ViewModelProvider
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_model, container, false)
        viewModelProvider = Common().initViewModelProvider(requireActivity().application, this)
        modelFragmentViewModel = viewModelProvider.get(ModelFragmentViewModel::class.java)
        modelFragmentViewModel.loadModels()

        initObserver()
        return thisView
    }

    fun initObserver(){
        modelFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner, Observer {
            if(it > 0){
                var modelAdaptor = ModelAdaptor(modelFragmentViewModel.getModelList().value!!, this.requireContext())
                var recyclerView = thisView.findViewById(R.id.models_recycler) as RecyclerView
                recyclerView.adapter = modelAdaptor
                // 设置布局，否则无法显示
                var linearLayoutManager = LinearLayoutManager(this.requireContext())
                recyclerView.layoutManager = linearLayoutManager
            }else if (it == 0){
                Snackbar.make(thisView.findViewById(R.id.models_recycler), "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
//                Common().alert(requireContext(), "无法连接服务器！！")
            }
        })
    }
}