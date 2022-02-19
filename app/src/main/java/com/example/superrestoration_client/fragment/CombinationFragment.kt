package com.example.superrestoration_client.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.superrestoration_client.R
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import kotlinx.android.synthetic.main.fragment_combination.*
import kotlinx.android.synthetic.main.fragment_model_combination.*

class CombinationFragment: Fragment() {
    private val TAG = "CombinationFragment"
    private lateinit var buttonAddCombination: Button
    private lateinit var thisView: View
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_combination, container, false)
        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_combination.setOnClickListener{
            shareViewModel.setIsSelectable(true)
            val navController = Navigation.findNavController(it)
//            navController.setGraph(R.navigation.navigation_graph_model_combination)
            navController.navigate(R.id.modelFragment)
        }
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