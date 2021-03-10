package com.mccarty.cloudcam3.ui.cameraview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewViewModel: ViewModel() {

    val showCameraButton = MutableLiveData<String>()

    fun cameraMode(mode: String) {
        showCameraButton.value = mode
    }
}