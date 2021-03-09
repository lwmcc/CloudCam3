package com.mccarty.cloudcam3.ui.cameraview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
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
import com.mccarty.cloudcam3.MainActivity
import com.mccarty.cloudcam3.MainActivityViewModel
import com.mccarty.cloudcam3.R
import kotlinx.android.synthetic.main.fragment_camera_view.*
import java.io.File
import java.util.concurrent.ExecutorService
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class CameraViewFragment: Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var finder: PreviewView
    private val TAG = CameraViewFragment::class.simpleName

    private var camera: Camera? = null

    private val model: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        finder = viewFinder
        outputDirectory = requireActivity().filesDir

        camera_capture_button.setOnClickListener {
            takePhoto()
        }

        switch_capture_mode.setOnCheckedChangeListener { buttonView, isChecked ->
            println("SWITCHED ")
        }

        video_capture_button.setOnClickListener {

        }

        startCamera()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.showButton(false)
    }

    override fun onResume() {
        super.onResume()

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

                imageCapture = ImageCapture.Builder().build()

                /*val imageAnalyzer = ImageAnalysis.Builder()
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                                Log.d(TAG, "Average luminosity: $luma")
                            })
                        }*/

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    //cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview)
                    camera = cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, imageCapture)

                    //cameraProvider.bindToLifecycle(
                    //        this, cameraSelector, preview, imageCapture, imageAnalyzer)

                } catch(exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        Log.d(TAG, "TAKE PHOTO")

        // Create time-stamped output file to hold the image
        val photoFile = File(
                outputDirectory,
                SimpleDateFormat("yyyy-MM-dd", Locale.US
                ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }
        })
    }

   /* private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }*/
}