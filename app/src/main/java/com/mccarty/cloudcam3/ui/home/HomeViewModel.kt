package com.mccarty.cloudcam3.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mccarty.cloudcam3.db.AppDatabase

class HomeViewModel @ViewModelInject constructor(private val addDatabase: AppDatabase): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}