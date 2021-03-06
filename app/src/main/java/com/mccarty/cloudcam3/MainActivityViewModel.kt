package com.mccarty.cloudcam3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class MainActivityViewModel: ViewModel() {
    val showCameraButton = MutableLiveData<Boolean>()

    fun showButton(show: Boolean) {
        showCameraButton.value = show
    }

}