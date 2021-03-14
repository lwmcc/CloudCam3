package com.mccarty.cloudcam3.ui.home

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

    val getAllMediaList = MutableLiveData<MutableList<ImageEntity>>()

    fun getAllMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaList = appDatabase.imageDao().getAll()
            getAllMediaList.postValue(mediaList)
        }
    }
}