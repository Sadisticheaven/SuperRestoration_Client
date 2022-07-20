package com.example.superrestoration_client

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.superrestoration_client.databinding.ActivityMainBinding
import com.example.superrestoration_client.fragment.main_act.*
import com.example.superrestoration_client.utils.Config
import com.example.superrestoration_client.utils.ViewPagerAdaptor
import com.example.superrestoration_client.view_model.ImageRestorationViewModel
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(){
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainActivityShareViewModel: MainActivityShareViewModel
    private lateinit var imageRestorationViewModel: ImageRestorationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityShareViewModel = ViewModelProvider(this)[MainActivityShareViewModel::class.java]
        mainActivityShareViewModel.setCurrentUser(intent.getParcelableExtra("currentUser")!!)
        imageRestorationViewModel = ViewModelProvider(this)[ImageRestorationViewModel::class.java]

        initView()
        initObserver()

    }

    private fun initObserver() {
        mainActivityShareViewModel.getCurrentUser().observe(this){
            if (it.getUserId() != -1){
                Snackbar.make(findViewById(R.id.snackbar_container), "欢迎${it.getUserName()}！！", Snackbar.LENGTH_LONG).show()
                Log.e(TAG, "欢迎${it.getUserName()}！！")
                mainActivityShareViewModel.requestUserCombinations()
                println(mainActivityShareViewModel.getCombinations())
            }
        }
        // jump to target fragment
        mainActivityShareViewModel.getNotify().observe(this){
            when(it.to){
                Config.fragmentTag["ModelFragment"] -> activityMainBinding.vpMain.currentItem = 1
                Config.fragmentTag["CombinationFragment"] -> activityMainBinding.vpMain.currentItem = 1
                Config.fragmentTag["DatasetFragment"] -> activityMainBinding.vpMain.currentItem = 2
                Config.fragmentTag["ModelSelectedFragment"] -> activityMainBinding.vpMain.currentItem = 3
                Config.fragmentTag["DatasetSelectedFragment"] -> activityMainBinding.vpMain.currentItem = 3
            }
        }
    }

    private fun initView() {
//        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host)
//        val navController = NavHostFragment.findNavController(navHost!!)
//        // 将navController与navBottom绑定
//        NavigationUI.setupWithNavController(activityMainBinding.navBottom, navController)
        val childFragments = arrayListOf(
            HomeFragment(), ModelCombinationFragment(),
            DatasetFragment(), ImageRestorationFragment(), UserFragment()
        )
        val vpAdaptor = ViewPagerAdaptor(childFragments, supportFragmentManager, lifecycle)
        activityMainBinding.vpMain.adapter = vpAdaptor
        TabLayoutMediator(activityMainBinding.navTabMain,
            activityMainBinding.vpMain
        ) { tab, position ->
            when(position) {
                0 -> tab.text = "首页"
                1 -> tab.text = resources.getString(R.string.algorithm)
                2 -> tab.text = resources.getString(R.string.dataset)
                3 -> tab.text = resources.getString(R.string.image_restore)
                4 -> tab.text = resources.getString(R.string.user)
            }
        }.attach()
    }
}