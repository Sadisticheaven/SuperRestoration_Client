package com.example.superrestoration_client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.superrestoration_client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        var navHost = supportFragmentManager.findFragmentById(R.id.nav_host)
        var navController = NavHostFragment.findNavController(navHost!!)
        // 将navController与navBottom绑定
        NavigationUI.setupWithNavController(activityMainBinding.navBottom, navController)
    }
}