package com.mccarty.cloudcam3.ui.imageview

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mccarty.cloudcam3.MainActivityViewModel
import com.mccarty.cloudcam3.ui.CameraFragments

class ImageViewFragment: Fragment(), CameraFragments {

    private val model: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            showHideFabButton(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showHideFabButton(false)
    }

    override fun showHideFabButton(showButton: Boolean) {
        model.showButton(showButton)
    }

}