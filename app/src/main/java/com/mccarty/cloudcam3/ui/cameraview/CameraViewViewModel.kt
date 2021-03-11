package com.mccarty.cloudcam3.ui.cameraview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewViewModel: ViewModel() {

    val showCameraButton = MutableLiveData<String>()
    val showPictureCaptureButton = MutableLiveData<Boolean>()

    fun cameraMode(mode: String) {
        showCameraButton.value = mode
    }

    fun showPicButton(show: Boolean) {
        when(show) {
            true -> showPictureCaptureButton.value = true
            false -> showPictureCaptureButton.value = false
        }
    }

    fun saveImageLocationToDb() {

    }
}