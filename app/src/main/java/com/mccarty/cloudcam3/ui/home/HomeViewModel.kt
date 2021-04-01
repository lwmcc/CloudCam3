package com.mccarty.cloudcam3.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private val appDatabase: AppDatabase): ViewModel() {

    private val _getAllMediaList = MutableLiveData<Array<ImageEntity>>()
    val getAllMediaList: LiveData<Array<ImageEntity>> = _getAllMediaList

    private val _showDeleteImageDialog = MutableLiveData<ImageEntity>()
    val showDeleteImageDialog: LiveData<ImageEntity> = _showDeleteImageDialog

    private val _goToImageView = MutableLiveData<ImageEntity>()
    val goToImageView: LiveData<ImageEntity> = _goToImageView

    private val _adapterChanged = MutableLiveData<Boolean>()
    val adapterChanged: LiveData<Boolean> = _adapterChanged

    fun getAllMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaList = appDatabase.imageDao().getAll()
            _getAllMediaList.postValue(mediaList)
        }
    }

    fun setConfirmDeleteImageDialogTrue(entity: ImageEntity) {
        _showDeleteImageDialog.value = entity
    }

    fun setDeleteImage() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.imageDao().deleteImage(showDeleteImageDialog.value!!)
        }
    }

    fun notifyAdapterChanged(changed: Boolean) {
        _adapterChanged.value = changed
    }
}