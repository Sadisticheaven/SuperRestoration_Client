package com.example.superrestoration_client

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.superrestoration_client.databinding.ActivityMainBinding
import com.example.superrestoration_client.fragment.ModelFragment
import com.example.superrestoration_client.utils.SharePreferenceUtil
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.example.superrestoration_client.view_model.ModelFragmentViewModel


class MainActivity : AppCompatActivity(){
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainActivityShareViewModel: MainActivityShareViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityShareViewModel = ViewModelProvider(this)[MainActivityShareViewModel::class.java]
        initView()
    }
    private fun initView() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host)
        val navController = NavHostFragment.findNavController(navHost!!)
        // 将navController与navBottom绑定
        NavigationUI.setupWithNavController(activityMainBinding.navBottom, navController)
    }

    fun addNewCombination(view: View) {
        ModelFragment().addNewCombination()
        view.visibility = View.GONE
        findViewById<Button>(R.id.finish_add_combination).visibility = View.VISIBLE
        findViewById<EditText>(R.id.combination_name).visibility = View.VISIBLE
    }

    fun finishAddCombination(view: View) {
        findViewById<Button>(R.id.add_combination).visibility = View.VISIBLE
        view.visibility = View.GONE
        findViewById<EditText>(R.id.combination_name).visibility = View.GONE
        mainActivityShareViewModel.addNewCombination(ModelFragmentViewModel().getNewCombinations())
    }
}