package com.mccarty.cloudcam3.ui.cameraview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraViewViewModel @ViewModelInject constructor(private val appDatabase: AppDatabase): ViewModel() {

    private val TAG = CameraViewViewModel::class.java.simpleName
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

    fun saveImageLocationToDb(entity: ImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.imageDao().insertImageEntity(entity)
        }
    }
}