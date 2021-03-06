package com.mccarty.cloudcam3

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mccarty.cloudcam3.ui.cameraview.CameraViewFragment
import com.mccarty.cloudcam3.ui.imageview.ImageViewFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val model: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            navController.navigate(R.id.cameraViewFragment)
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

    override fun onStart() {
        super.onStart()
        // TODO:
    }

    override fun onResume() {
        super.onResume()
        // TODO:
    }

    override fun onPause() {
        super.onPause()
        // TODO:
    }

    override fun onStop() {
        super.onStop()
        // TODO:
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        showHideFabButton(true)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}