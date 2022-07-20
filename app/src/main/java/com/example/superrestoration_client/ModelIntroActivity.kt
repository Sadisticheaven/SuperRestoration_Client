package com.example.superrestoration_client

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.superrestoration_client.databinding.ActivityModelIntroBinding
import com.example.superrestoration_client.databinding.ActivitySuperResolutionBinding
import com.example.superrestoration_client.fragment.superresolution_act.CompareFragment
import com.example.superrestoration_client.fragment.superresolution_act.MultiAlgorithmFragment
import com.example.superrestoration_client.fragment.superresolution_act.SelectFragment
import com.example.superrestoration_client.fragment.superresolution_act.SingleAlgorithmFragment
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.SuperRestorationActivityViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ModelIntroActivity : AppCompatActivity() {
    private val TAG = "ModelIntroActivity"
    private lateinit var activityModelIntroBinding: ActivityModelIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityModelIntroBinding = DataBindingUtil.setContentView(this, R.layout.activity_model_intro)
        val modelName = intent.getStringExtra("modelName")!!

        initView()
        // 加载网页
        activityModelIntroBinding.web.loadUrl(Config.webBaseUrl+"/model_list/"+modelName)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        activityModelIntroBinding.web.apply {
            // 启用JS
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT//存储的最大容量
            settings.allowFileAccess = true
        }
    }

}