package com.mccarty.cloudcam3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mccarty.cloudcam3.ui.cameraview.CameraViewFragment
import com.mccarty.cloudcam3.ui.imageview.ImageViewFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.canonicalName
    private lateinit var fab: FloatingActionButton
    private lateinit var navController: NavController
    private val model: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            navigateToCamera()
        }

        model.showCameraButton.observe(this, Observer<Boolean> {
            showHideFabButton(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun showHideFabButton(show: Boolean) {
        model.showCameraButton.observe(this, Observer<Boolean> {
            when(show) {
                true -> fab.isVisible = true
                false -> fab.isGone = true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        showHideFabButton(true)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE_PERMISSION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToCamera(R.id.cameraViewFragment)
            }
        }
    }

    private fun navigateToCamera() {
        when(hasCameraPermission()) {
            true -> navigateToCamera(R.id.cameraViewFragment)
            false -> requestCameraPermission()
        }
     }

    private fun navigateToCamera(id: Int) {
        navController.navigate(id)
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION), CAMERA_REQUEST_CODE_PERMISSION);
    }

    companion object {
        const val CAMERA_REQUEST_CODE_PERMISSION = 101
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

}