package com.mccarty.cloudcam3.ui.cameraview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.util.concurrent.ListenableFuture
import com.mccarty.cloudcam3.MainActivityViewModel
import com.mccarty.cloudcam3.R
import kotlinx.android.synthetic.main.fragment_camera_view.*
import java.io.File
import java.util.concurrent.ExecutorService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class CameraViewFragment: Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var finder: PreviewView

    private var camera: Camera? = null

    private val model: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))

        println("ON CREATE *****")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        finder = viewFinder

        camera_capture_button.setOnClickListener {
                println("CLICK TAKE IMAGE")
        }

        switch_capture_mode.setOnCheckedChangeListener { buttonView, isChecked ->
            println("SWITCHED ")
        }

        video_capture_button.setOnClickListener {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.showButton(false)
    }

    override fun onResume() {
        super.onResume()
        println("ON RESUME *****")
        startCamera()
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        println("REQUEST $requestCode *****")

    }

    fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder().build()

        var cameraSelector : CameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview.setSurfaceProvider(finder.createSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                    }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview)

            } catch(exc: Exception) {
               println("ERROR $exc *****")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }
}