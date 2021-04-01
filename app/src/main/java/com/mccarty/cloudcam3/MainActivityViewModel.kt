package com.mccarty.cloudcam3

import android.media.Image
import android.net.Uri
import androidx.lifecycle.*
import com.mccarty.cloudcam3.db.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Entity
import java.io.File

class MainActivityViewModel: ViewModel() {
    private val _showCameraButton = MutableLiveData<Boolean>(true)
    val showCameraButton: LiveData<Boolean> = _showCameraButton

    private val _goToImageView = MutableLiveData<ImageEntity>() // TODO: change this name
    val goToImageView: LiveData<ImageEntity> = _goToImageView

    private val _imageFile = MutableLiveData<Uri>()
    val imageFile: LiveData<Uri> = _imageFile

    private val _navigateToImage = MutableLiveData<ImageEntity>()
    val navigateToImage: LiveData<ImageEntity> = _navigateToImage

    fun showButton(show: Boolean) {
        _showCameraButton.value = show
    }

    fun navigateToImageView(entity: ImageEntity) {
        _navigateToImage.value = entity
    }

    fun getImageForView() {
        viewModelScope.launch(Dispatchers.IO) {
            _goToImageView.value?.localFilePath?.let {
                val uri = Uri.fromFile(File(it))
                _imageFile.postValue(uri)
            }
        }
    }
}