package com.example.superrestoration_client.fragment.main_act

import android.content.Intent
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.superrestoration_client.ModelIntroActivity
import com.example.superrestoration_client.R
import com.example.superrestoration_client.SuperResolutionActivity
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
    private lateinit var refreshLayout: SwipeRefreshLayout
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
        refreshLayout = fragmentModelBinding.refreshModel
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
                    Config.fragmentTag["CombinationFragment"] -> fromCombinationFragment() // 由combination跳转过来，表示新建组合
                    Config.fragmentTag["ModelSelectedFragment"] -> fromModelSelectedFragment()
                    else ->  switchItemSelectable(false) // 恢复本界面控件状态
                }
            }
        }

        refreshLayout.setOnRefreshListener {
            modelFragmentViewModel.loadModels(shareViewModel)
            if (refreshLayout.isRefreshing)
                refreshLayout.isRefreshing = false
        }
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

            override fun onItemClick(view: View, position: Int) {
                println("click item: $position")
                val intent = Intent()
                intent.setClass(requireContext(), ModelIntroActivity::class.java)
                intent.putExtra("modelName", shareViewModel.getModelList().value!![position].getModelName())
                startActivity(intent)
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
        if (selectable)
            recyclerAdaptor.setItemVisibility(ModelAdaptor.ItemVisibility(View.VISIBLE, View.GONE))
        else
            recyclerAdaptor.setItemVisibility(ModelAdaptor.ItemVisibility(View.GONE, View.GONE))
        // 需要重新设置adapter才能刷新界面
        recyclerView.adapter = recyclerAdaptor
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