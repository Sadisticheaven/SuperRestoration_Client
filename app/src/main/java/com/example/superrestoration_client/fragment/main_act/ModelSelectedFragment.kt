package com.example.superrestoration_client.fragment.main_act

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.ModelIntroActivity
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentModelSelectedBinding
import com.example.superrestoration_client.model.Model
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.FragmentNotify
import com.example.superrestoration_client.utils.ModelAdaptor
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_model_selected.*

class ModelSelectedFragment : Fragment() {
    private val TAG = "ModelSelectedFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentModelSelectedBinding: FragmentModelSelectedBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: ModelAdaptor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentModelSelectedBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_model_selected, container, false)
        recyclerView = fragmentModelSelectedBinding.rycSelectedModels
        initObserver()
        return fragmentModelSelectedBinding.root
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner) {
            if (it.to == Config.fragmentTag[TAG]
                && it.from == Config.fragmentTag["ModelFragment"]){
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "刷新列表！！", Snackbar.LENGTH_LONG).show()
                var selectedModels = ArrayList<Model>()
                for (idx in shareViewModel.getSelectedModelsIndex())
                    selectedModels.add(shareViewModel.getModelList().value!![idx])
                // 可能会再modelFragment删除之前选择的model，因此要重新赋值而不是直接addAll
                var selectedModelsIndex = arrayListOf<Int>()
                selectedModelsIndex.addAll(shareViewModel.getSelectedModelsIndex())
                imageRestorationViewModel.getSelectedModelsIndex().value = selectedModelsIndex
                updateItems(selectedModels)
            }
        }
    }

    private fun updateItems(selectedModels: ArrayList<Model>){
        recyclerAdaptor = ModelAdaptor(selectedModels, this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: ModelAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                recyclerAdaptor.removeItem(position)
                imageRestorationViewModel.getSelectedModelsIndex().value!!.removeAt(position)
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "移除$position 成功！！", Snackbar.LENGTH_LONG).show()
            }

            override fun onItemClick(view: View, position: Int) {
                println("click item: $position")
                val intent = Intent()
                intent.setClass(requireContext(), ModelIntroActivity::class.java)
                intent.putExtra("modelName", shareViewModel.getModelList().value!![position].getModelName())
                startActivity(intent)
            }
        })
        recyclerAdaptor.setItemVisibility(ModelAdaptor.ItemVisibility(View.GONE, View.VISIBLE))
        recyclerView.adapter = recyclerAdaptor
        // 设置布局，否则无法显示
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
//        childFragmentManager.executePendingTransactions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_models_or_combinations.setOnClickListener{
            var selectedModels = mutableSetOf<Int>()
            selectedModels.addAll(imageRestorationViewModel.getSelectedModelsIndex().value!!)
            shareViewModel.setSelectedModelsIndex(selectedModels)
            shareViewModel.getNotify().postValue(
                FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["ModelFragment"]!!)
            )
        }
    }
}