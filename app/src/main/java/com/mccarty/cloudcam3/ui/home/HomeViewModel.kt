package com.mccarty.cloudcam3.ui.home

import android.media.Image
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.db.MediaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private val appDatabase: AppDatabase): ViewModel() {

    val getAllMediaList = MutableLiveData<Array<ImageEntity>>()
    val showDeleteImageDialog = MutableLiveData<ImageEntity>()
    val adapterChanged = MutableLiveData<Boolean>()

    fun getAllMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaList = appDatabase.imageDao().getAll()
            getAllMediaList.postValue(mediaList)
        }
    }

    fun goToImageView() {
        println("GO TO IMAGE VIEW *****")
    }

    fun setConfirmDeleteImageDialogTrue(entity: ImageEntity) {
        showDeleteImageDialog.value = entity
    }

    fun setDeleteImage() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.imageDao().deleteImage(showDeleteImageDialog.value!!)
        }
    }

    fun notifyAdapterChanged(changed: Boolean) {
        adapterChanged.value = changed
    }
}