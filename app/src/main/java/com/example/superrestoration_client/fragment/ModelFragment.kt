package com.example.superrestoration_client.fragment

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
import com.example.superrestoration_client.databinding.ActivityLoginBinding
import com.example.superrestoration_client.databinding.FragmentModelBinding
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * 显示模型列表的界面
 */
class ModelFragment:Fragment() {
    private lateinit var modelFragmentViewModel: ModelFragmentViewModel
    private lateinit var thisView: View
    private lateinit var recyclerView: RecyclerView
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        thisView = inflater.inflate(R.layout.fragment_model, container, false)
        val fragmentModelBinding: FragmentModelBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_model, container, false)
        thisView = fragmentModelBinding.root

        modelFragmentViewModel = ViewModelProvider(this)[ModelFragmentViewModel::class.java]
        modelFragmentViewModel.loadModels(shareViewModel)
        // 需要在此初始化并绑定adapter避免"No adapter attached; skipping layout"
        recyclerView = thisView.findViewById(R.id.models_recycler) as RecyclerView
        updateItems()
        initObserver()
        fragmentModelBinding.apply {
            modelViewModel = modelFragmentViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return thisView
    }

    private fun initObserver(){
        modelFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner, {
            if(it > 0){
                updateItems()
            }else if (it == 0){
                Snackbar.make(thisView.findViewById(R.id.models_recycler), "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
//                Common().alert(requireContext(), "无法连接服务器！！")
            }
        })

        // 由于Fragment切换时是异步commit，因此在回调中更改按钮状态
        recyclerView.viewTreeObserver.addOnDrawListener {
            for (idx in shareViewModel.getSelectedModels())
                switchButton(idx, true)
        }

    }

    private fun updateItems(){
        val adaptor = ModelAdaptor(shareViewModel.getModelList().value!!, this.requireContext())
        // 实现Item中控件的回调
        adaptor.setOnItemClickListener(object: ModelAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (shareViewModel.addModel(position)){
                    switchButton(position, true)

                    println(shareViewModel.getSelectedModels())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.models_recycler), "添加$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                if (shareViewModel.removeModel(position)){
                    switchButton(position, false)
                    println(shareViewModel.getSelectedModels())
                }else{
                    Snackbar.make(thisView.findViewById(R.id.models_recycler), "移除$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        recyclerView.adapter = adaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
    }

    fun switchButton(position: Int, selected: Boolean){
        val itemView =  recyclerView.layoutManager!!.findViewByPosition(position)
        val addButton: ImageButton = itemView!!.findViewById(R.id.add_model_to_list)
        val removeButton: ImageButton = itemView.findViewById(R.id.remove_model_from_list)
        if (selected){
            addButton.visibility = View.GONE
            removeButton.visibility = View.VISIBLE
        }else{
            addButton.visibility = View.VISIBLE
            removeButton.visibility = View.GONE
        }
    }

    fun addNewCombination(){
    }
}