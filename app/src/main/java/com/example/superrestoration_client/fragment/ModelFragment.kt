package com.example.superrestoration_client.fragment

import android.os.Bundle
import android.util.Log
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
import com.example.superrestoration_client.databinding.FragmentModelBinding
import com.example.superrestoration_client.model.Combination
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_model.*
import java.util.ArrayList

/**
 * 显示模型列表的界面
 */
class ModelFragment:Fragment() {
    private val TAG = "ModelFragment"
    private lateinit var modelFragmentViewModel: ModelFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentModelBinding: FragmentModelBinding
    private lateinit var recyclerAdaptor: ModelAdaptor
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentModelBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_model, container, false)

        modelFragmentViewModel = ViewModelProvider(this)[ModelFragmentViewModel::class.java]
        modelFragmentViewModel.loadModels(shareViewModel)
        // 需要在此初始化并绑定adapter避免"No adapter attached; skipping layout"
        recyclerView = fragmentModelBinding.recyclerModels
        initObserver()
        fragmentModelBinding.apply {
            modelViewModel = modelFragmentViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentModelBinding.root
    }

    private fun initObserver(){
        modelFragmentViewModel.getRequestStatus().observe(viewLifecycleOwner) {
            if (it > 0) {
                updateItems()
                Log.e(TAG, "models num = ${recyclerView.layoutManager!!.itemCount}")
            } else if (it == 0) {
                Snackbar.make(
                    requireActivity().findViewById(R.id.snackbar_container),
                    "无法连接服务器！！",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        shareViewModel.getNotify().observe(viewLifecycleOwner){
            if (it.to == Config.fragmentTag[TAG]){
                when(it.from) {
                    Config.fragmentTag["CombinationFragment"] ->  fromCombinationFragment() // 由combination跳转过来，表示新建组合
                    Config.fragmentTag["ModelSelectedFragment"] -> fromModelSelectedFragment()
                    else ->  switchItemSelectable(false) // 恢复本界面控件状态
                }
            }
        }

//        shareViewModel.getIsSelectable().observe(viewLifecycleOwner){
//            switchItemSelectable(it)
//        }

        // 由于Fragment切换时是异步commit，因此在回调中更改按钮状态
//        recyclerView.viewTreeObserver.addOnDrawListener {
//            switchItemSelectable(shareViewModel.getIsSelectable().value!!)
//            if (shareViewModel.getIsSelectable().value!!){
//                for (idx in shareViewModel.getSelectedModels())
//                    switchSelectButtonStatus(idx, true)
//            }
//        }
    }



    private fun updateItems(){
        recyclerAdaptor = ModelAdaptor(shareViewModel.getModelList().value!!, this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: ModelAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (shareViewModel.addModel(position)){
                    switchSelectButtonStatus(position, true)
                    println(shareViewModel.getSelectedModelsIndex())
                }else{
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container), "添加$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                if (shareViewModel.removeModel(position)){
                    switchSelectButtonStatus(position, false)
                    println(shareViewModel.getSelectedModelsIndex())
                }else{
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container), "移除$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }
        })
        recyclerView.adapter = recyclerAdaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        childFragmentManager.executePendingTransactions()
    }

    fun switchSelectButtonStatus(position: Int, selected: Boolean){
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

    private fun switchItemSelectable(selectable: Boolean){
        if (selectable){
            for (position in 0 until recyclerView.layoutManager!!.itemCount){
                val itemView =  recyclerView.layoutManager!!.findViewByPosition(position)
                itemView!!.findViewById<ImageButton>(R.id.add_model_to_list).visibility = View.VISIBLE
                itemView.findViewById<ImageButton>(R.id.remove_model_from_list).visibility = View.GONE
            }
        }
        else{
            for (position in 0 until recyclerView.layoutManager!!.itemCount){
                val itemView =  recyclerView.layoutManager!!.findViewByPosition(position)
                itemView!!.findViewById<ImageButton>(R.id.add_model_to_list).visibility = View.GONE
                itemView.findViewById<ImageButton>(R.id.remove_model_from_list).visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_model_finish.setOnClickListener{
            when(shareViewModel.getNotify().value!!.from){
                Config.fragmentTag["CombinationFragment"] -> backToCombinationFragment()
                Config.fragmentTag["ModelSelectedFragment"] -> backToModelSelectedFragment()
            }
        }
    }

    private fun fromCombinationFragment(){
        fragmentModelBinding.addModelFinish.visibility = View.VISIBLE
        fragmentModelBinding.combinationName.visibility = View.VISIBLE
        switchItemSelectable(true)
    }

    private fun backToCombinationFragment(){
        fragmentModelBinding.addModelFinish.visibility = View.GONE
        fragmentModelBinding.combinationName.visibility = View.GONE
        switchItemSelectable(false)
        val models = ArrayList<Int>()
        models.addAll(shareViewModel.getSelectedModelsIndex())
        val newCombination = Combination()
        newCombination.setcombinationList(models)
        newCombination.setOwnerId(shareViewModel.getCurrentUser().value!!.getUserId())
        newCombination.setName(modelFragmentViewModel.getNewCombinationName())
        shareViewModel.addNewCombination(newCombination)
        shareViewModel.getNotify().postValue(
            FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["CombinationFragment"]!!))
    }

    private fun fromModelSelectedFragment() {
        fragmentModelBinding.addModelFinish.visibility = View.VISIBLE
        switchItemSelectable(true)
        for (idx in shareViewModel.getSelectedModelsIndex())
            switchSelectButtonStatus(idx, true)
    }

    private fun backToModelSelectedFragment(){
        fragmentModelBinding.addModelFinish.visibility = View.GONE
        switchItemSelectable(false)
        shareViewModel.getNotify().postValue(
            FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["ModelSelectedFragment"]!!))
    }

    override fun onPause() {
        Log.e(TAG, "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.e(TAG, "onResume")
        super.onResume()
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onStop() {
        Log.e(TAG, "onStop")
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }
}