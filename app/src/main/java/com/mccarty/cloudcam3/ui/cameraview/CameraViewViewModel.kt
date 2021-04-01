package com.mccarty.cloudcam3.ui.cameraview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraViewViewModel @ViewModelInject constructor(private val repo: Repository): ViewModel() {

    private val _showCameraButton = MutableLiveData<String>("")
    val showCameraButton: LiveData<String> = _showCameraButton

    private val _showPictureCaptureButton = MutableLiveData<Boolean>(false)
    val showPictureCaptureButton: LiveData<Boolean> = _showPictureCaptureButton

    fun cameraMode(mode: String) {
        _showCameraButton.value = mode
    }

    fun showPicButton(show: Boolean) {
        when(show) {
            true -> _showPictureCaptureButton.value = true
            false -> _showPictureCaptureButton.value = false
        }
    }

    fun saveImageLocationToDb(entity: ImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertEntity(entity)
        }
    }
}