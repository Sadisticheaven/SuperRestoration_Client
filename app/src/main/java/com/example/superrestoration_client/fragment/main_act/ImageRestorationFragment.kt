package com.example.superrestoration_client.fragment.main_act

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.superrestoration_client.R
import com.example.superrestoration_client.databinding.FragmentImageRestorationBinding
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_image_restoration.*

class ImageRestorationFragment : Fragment() {
    private val TAG = "ImageRestorationFragment"
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private val imageRestorationViewModel: ImageRestorationViewModel by activityViewModels()
    private lateinit var fragmentImageRestorationBinding: FragmentImageRestorationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentImageRestorationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_restoration, container, false)
        initView()
        initObserver()
        return fragmentImageRestorationBinding.root
    }

    private fun initView() {
        val childFragments = arrayListOf(ModelSelectedFragment(), DatasetSelectedFragment(), HistoryFragment())
        val vpAdaptor = ViewPagerAdaptor(childFragments, childFragmentManager, lifecycle)
        fragmentImageRestorationBinding.vpImageRestoration.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    2 -> fragmentImageRestorationBinding.runModelsOnDatasets.visibility = View.GONE
                    else -> fragmentImageRestorationBinding.runModelsOnDatasets.visibility = View.VISIBLE
                }
            }
        })
        fragmentImageRestorationBinding.vpImageRestoration.adapter = vpAdaptor
        TabLayoutMediator(fragmentImageRestorationBinding.navTabImageRestoration,
            fragmentImageRestorationBinding.vpImageRestoration
        ) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.algorithm)
                1 -> tab.text = resources.getString(R.string.dataset)
                2 -> tab.text = resources.getString(R.string.result)
            }
        }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        fragmentImageRestorationBinding.runModelsOnDatasets.setOnClickListener{
//            Log.e(TAG, "send modelsId and datasetsId to server")
//        }
        run_models_on_datasets.setOnClickListener{
            imageRestorationViewModel.runModelsOnDatasets(
                shareViewModel.getCurrentUser().value!!.getUserId(),
                shareViewModel.getModelList().value!!,
                shareViewModel.getDatasetList().value!!)
        }
    }

    private fun initObserver() {
        shareViewModel.getNotify().observe(viewLifecycleOwner){
            when(it.to){
                Config.fragmentTag["ModelSelectedFragment"] -> {
                    fragmentImageRestorationBinding.vpImageRestoration.currentItem = 0
                }
                Config.fragmentTag["DatasetSelectedFragment"] -> {
                    fragmentImageRestorationBinding.vpImageRestoration.currentItem = 1
                }
                Config.fragmentTag["ResultFragment"] -> {
                    fragmentImageRestorationBinding.vpImageRestoration.currentItem = 2
                }
            }
        }
        imageRestorationViewModel.getRestorationRequestCode().observe(viewLifecycleOwner){
            when(it){
                0 -> Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "无法连接服务器！！", Snackbar.LENGTH_LONG).show()
                1 -> Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "请先选择模型和数据集！！", Snackbar.LENGTH_LONG).show()
                2 -> Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "用户不存在！！", Snackbar.LENGTH_LONG).show()
                99 -> Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "服务器程序运行错误！", Snackbar.LENGTH_LONG).show()
                200 -> {
                    Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                        "提交成功，请等待服务器处理~~", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


}