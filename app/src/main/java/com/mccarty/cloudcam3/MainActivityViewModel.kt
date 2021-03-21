package com.mccarty.cloudcam3

import android.net.Uri
import androidx.lifecycle.*
import com.mccarty.cloudcam3.db.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Entity
import java.io.File

class MainActivityViewModel: ViewModel() {
    val showCameraButton = MutableLiveData<Boolean>()
    val goToImageView = MutableLiveData<ImageEntity>() // TODO: change this name
    val imageFile = MutableLiveData<Uri>()
    var navigateToImage = MutableLiveData<ImageEntity>()

    fun showButton(show: Boolean) {
        showCameraButton.value = show
    }

    fun navigateToImageView(entity: ImageEntity) {
        navigateToImage.value = entity
    }

    fun getImageForView() {
        viewModelScope.launch(Dispatchers.IO) {
            goToImageView.value?.localFilePath?.let {
                val uri = Uri.fromFile(File(it))
                imageFile.postValue(uri)
            }
        }
    }
}