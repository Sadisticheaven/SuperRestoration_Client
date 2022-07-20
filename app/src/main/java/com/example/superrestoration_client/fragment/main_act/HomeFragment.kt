package com.example.superrestoration_client.fragment.main_act

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.superrestoration_client.R
import com.example.superrestoration_client.SuperResolutionActivity
import com.example.superrestoration_client.view_model.MainActivityShareViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val shareViewModel: MainActivityShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "成功。", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(requireActivity().findViewById(R.id.snackbar_container),
                    "很遗憾，无法从手机读取图片。", Snackbar.LENGTH_LONG).show()
            }
        }
        btn_super_resolution.setOnClickListener{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }else{
                val intent = Intent()
                intent.setClass(requireContext(), SuperResolutionActivity::class.java)
                intent.putExtra("currentUser", shareViewModel.getCurrentUser().value)
                startActivity(intent)
            }
        }
    }

}