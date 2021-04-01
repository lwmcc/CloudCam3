package com.mccarty.cloudcam3.ui.home

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.hilt.lifecycle.ViewModelInject
import com.mccarty.cloudcam3.MainApplication
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.di.AppModule
import dagger.hilt.android.qualifiers.ApplicationContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HomeViewModelTest () {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var context: Context

    //@Mock
    private lateinit var db: AppDatabase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        db = mock(AppDatabase::class.java)

        //db = AppDatabase.getDatabase(context)
        homeViewModel = spy(HomeViewModel(db))
    }

    @Test
    fun getGetAllMediaList() {


        `when`(homeViewModel.getAllMedia()).thenReturn( getImageEntityArray() )
        homeViewModel.getAllMedia()
        //val allMedia = homeViewModel.getAllMedia()




        //println("DATA")
    }

    private fun getImageEntityArray() {
        val array = arrayOf(getImageEntity())
    }

    private fun getImageEntity(): ImageEntity {
        return ImageEntity(fileName = "name", localFilePath = "path", userName = "user")
    }

}