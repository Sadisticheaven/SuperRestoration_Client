package com.example.superrestoration_client.fragment.superresolution_act

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentMultiAlgorithmBinding
import com.example.superrestoration_client.utils.*
import com.example.superrestoration_client.view_model.MultiAlgorithmFragmentViewModel
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.snackbar.Snackbar


class MultiAlgorithmFragment : Fragment() {
    private val TAG = "MultiAlgorithmFragment"
    private lateinit var multiAlgorithmFragmentViewModel: MultiAlgorithmFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentMultiAlgorithmBinding: FragmentMultiAlgorithmBinding
    private lateinit var recyclerAdaptor: ModelNameAdaptor
    private val superRestorationActivityViewModel: SuperRestorationActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMultiAlgorithmBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_multi_algorithm, container, false)
        multiAlgorithmFragmentViewModel = ViewModelProvider(this)[MultiAlgorithmFragmentViewModel::class.java]
        recyclerView = fragmentMultiAlgorithmBinding.recyclerModelsSuperResolution
        initObserver()
        return fragmentMultiAlgorithmBinding.root
    }

    fun initObserver(){
        superRestorationActivityViewModel.getModelRequestStatus().observe(viewLifecycleOwner){
            if(it > 0){
                updateItems()
            }
        }
        multiAlgorithmFragmentViewModel.getRestoreRequestStatus().observe(viewLifecycleOwner){
            when(it){
                -1 -> {}
                200 -> {
                    superRestorationActivityViewModel.getNotify().postValue(
                        FragmentNotify(Config.fragmentTag[TAG]!!, Config.fragmentTag["CompareFragment"]!!))
                    superRestorationActivityViewModel.setSelectedModelsName(
                        multiAlgorithmFragmentViewModel.getSelectedModelsName())
                    superRestorationActivityViewModel.setImagesUrl(
                        multiAlgorithmFragmentViewModel.getImagesUrl())
                }
                else -> Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                    "请重试！", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateItems(){
        recyclerAdaptor = ModelNameAdaptor(ArrayList(superRestorationActivityViewModel.getModelsMap().keys), this.requireContext())
        // 实现Item中控件的回调
        recyclerAdaptor.setOnItemClickListener(object: ModelNameAdaptor.OnItemClickListener{
            override fun onAddButtonClick(view: View, position: Int) {
                if (multiAlgorithmFragmentViewModel.addModel(recyclerView.layoutManager
                        ?.findViewByPosition(position)
                        ?.findViewById<TextView>(R.id.text)?.text.toString())){
                    switchSelectButtonStatus(position, true)
                }else{
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container), "添加$position 失败！！", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onRemoveButtonClick(view: View, position: Int) {
                if (multiAlgorithmFragmentViewModel.removeModel(recyclerView.layoutManager
                        ?.findViewByPosition(position)
                        ?.findViewById<TextView>(R.id.text)?.text.toString())){
                    switchSelectButtonStatus(position, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMultiAlgorithmBinding.radioGroupScale.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                fragmentMultiAlgorithmBinding.scale2.id -> superRestorationActivityViewModel.setScaleSelected("x2")
                fragmentMultiAlgorithmBinding.scale3.id -> superRestorationActivityViewModel.setScaleSelected("x3")
                fragmentMultiAlgorithmBinding.scale4.id -> superRestorationActivityViewModel.setScaleSelected("x4")
                fragmentMultiAlgorithmBinding.scale8.id -> superRestorationActivityViewModel.setScaleSelected("x8")
            }
        }
        fragmentMultiAlgorithmBinding.runModelsOnPicture.setOnClickListener {
            val res = multiAlgorithmFragmentViewModel.runModelsOnPicture(requireContext(),
                superRestorationActivityViewModel.getCurrentUser()!!,
                superRestorationActivityViewModel.getCurrentImage().value,
                superRestorationActivityViewModel.getModelsMap(),
                superRestorationActivityViewModel.getScaleSelected())
            if (!res){
                Snackbar.make(requireActivity().findViewById(R.id.snackBar_superRestoration),
                    "请选择图片或模型！", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}