package com.mccarty.cloudcam3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mccarty.cloudcam3.db.ImageEntity

class MainActivityViewModel: ViewModel() {
    val showCameraButton = MutableLiveData<Boolean>()
    val goToImageView = MutableLiveData<ImageEntity>()

    fun showButton(show: Boolean) {
        showCameraButton.value = show
    }

    fun navigateToImageView(entity: ImageEntity) {
        println("GO TO IMAGE IN VM *****")
        goToImageView.value = entity
    }
}