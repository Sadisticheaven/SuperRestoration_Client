package com.example.superrestoration_client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.utils.Common
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * 显示模型列表的界面
 */
class ModelFragment:Fragment() {
    private lateinit var modelFragmentViewModel: ModelFragmentViewModel
    private lateinit var thisView: View
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_model, container, false)
        viewModelProvider = Common().initViewModelProvider(requireActivity().application, this)
        modelFragmentViewModel = viewModelProvider.get(ModelFragmentViewModel::class.java)
        modelFragmentViewModel.loadModels()
        // 需要在此初始化并绑定adapter避免"No adapter attached; skipping layout"
        recyclerView = thisView.findViewById(R.id.models_recycler) as RecyclerView
        updateItems()
        initObserver()
        return thisView
    }

    fun initObserver(){
        modelFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner, Observer {
            if(it > 0){
                updateItems()
            }else if (it == 0){
                Snackbar.make(thisView.findViewById(R.id.models_recycler), "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
//                Common().alert(requireContext(), "无法连接服务器！！")
            }
        })
    }

    fun updateItems(){
        var adaptor = ModelAdaptor(modelFragmentViewModel.getModelList().value!!, this.requireContext())
        // 实现Item中控件的回调
        adaptor.setOnItemClickListener(object: ModelAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (modelFragmentViewModel.addModel(position)){
                    switchButton(position, true)
                    println(modelFragmentViewModel.getSelectList())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.models_recycler), "添加$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                if (modelFragmentViewModel.removeModel(position)){
                    switchButton(position, false)
                    println(modelFragmentViewModel.getSelectList())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.models_recycler), "移除$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        recyclerView.adapter = adaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
        // 由于Fragment切换时是异步commit，因此在回调中更改按钮状态
        recyclerView.viewTreeObserver.addOnDrawListener {
            for (idx in modelFragmentViewModel.getSelectList())
                switchButton(idx, true)
        }
    }

    fun switchButton(position: Int, selected: Boolean){
        var itemView =  recyclerView.layoutManager!!.findViewByPosition(position)
        var addButton: ImageButton = itemView!!.findViewById(R.id.add_model_to_list)
        var removeButton: ImageButton = itemView.findViewById(R.id.remove_model_from_list)
        if (selected){
            addButton.visibility = View.GONE
            removeButton.visibility = View.VISIBLE
        }else{
            addButton.visibility = View.VISIBLE
            removeButton.visibility = View.GONE
        }
    }
}