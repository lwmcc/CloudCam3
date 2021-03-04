package com.mccarty.cloudcam3.ui.cameraview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mccarty.cloudcam3.R

class CameraViewFragment: Fragment() {

    companion object {
        fun newInstance() = CameraViewFragment()
    }

    private lateinit var viewModel: CameraViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraViewViewModel::class.java)
        // TODO: Use the ViewModel
    }
}