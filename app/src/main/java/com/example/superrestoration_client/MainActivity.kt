package com.example.superrestoration_client

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.superrestoration_client.databinding.ActivityMainBinding
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(){
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainActivityShareViewModel: MainActivityShareViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityShareViewModel = ViewModelProvider(this)[MainActivityShareViewModel::class.java]
        mainActivityShareViewModel.setCurrentUser(intent.getParcelableExtra("currentUser")!!)
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
    }

    private fun initView() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host)
        val navController = NavHostFragment.findNavController(navHost!!)
        // 将navController与navBottom绑定
        NavigationUI.setupWithNavController(activityMainBinding.navBottom, navController)
    }

//    fun addNewCombination(view: View) {
//        mainActivityShareViewModel.setIsSelectable(true)
//
//    }

    fun finishAddCombination(view: View) {
        mainActivityShareViewModel.addNewCombination(ModelFragmentViewModel().getNewCombinations())
        mainActivityShareViewModel.setIsSelectable(false)
    }
}