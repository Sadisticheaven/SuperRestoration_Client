package com.example.superrestoration_client.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.superrestoration_client.R
import com.example.superrestoration_client.view_model.MainActivityShareViewModel

class ResultFragment : Fragment() {
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    private lateinit var thisView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        thisView = inflater.inflate(R.layout.fragment_result, container, false)
        println(shareViewModel.getSelectedModels())
        return thisView
    }
}