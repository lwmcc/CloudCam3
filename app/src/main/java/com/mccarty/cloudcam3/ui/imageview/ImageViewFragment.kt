package com.mccarty.cloudcam3.ui.imageview

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mccarty.cloudcam3.MainActivityViewModel
import com.mccarty.cloudcam3.R
import com.mccarty.cloudcam3.ui.CameraFragments
import kotlinx.android.synthetic.main.fragment_image_view_layout.*
import java.io.File
import java.net.URI

class ImageViewFragment: Fragment(), CameraFragments {

    private val mainModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            showHideFabButton(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_image_view.setImageURI(Uri.fromFile(File(mainModel.navigateToImage.value?.localFilePath)))
        showHideFabButton(false)
    }

    override fun onStart() {
        super.onStart()
        uiSetup()
    }

    override fun showHideFabButton(showButton: Boolean) {
        mainModel.showButton(showButton)
    }

    override fun uiSetup() {
        mainModel.getImageForView()
    }

}