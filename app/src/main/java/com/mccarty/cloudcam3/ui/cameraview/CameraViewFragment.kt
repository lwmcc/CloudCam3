package com.mccarty.cloudcam3.ui.cameraview

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import java.lang.Integer.max
import java.lang.Math.abs
import java.lang.Math.min
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import androidx.lifecycle.Observer
import androidx.room.Room
import com.mccarty.cloudcam3.MainApplication
import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

typealias LumaListener = (luma: Double) -> Unit

@AndroidEntryPoint
class CameraViewFragment: Fragment() {

    private val TAG = CameraViewFragment::class.simpleName
    private val model: MainActivityViewModel by activityViewModels()
    private val cameraModel: CameraViewViewModel by viewModels()

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var finder: PreviewView

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var imageCapture: ImageCapture? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraModel.cameraMode(CameraModes.PHOTO.mode)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            model.showButton(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        finder = viewFinder
        outputDirectory = requireActivity().filesDir

        setupUI()
        startCamera()

        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraModel.showCameraButton.observe(requireActivity(), Observer<String> {
            setCaptureModeButtonImage(it)
        })

        cameraModel.showPictureCaptureButton.observe(requireActivity(), Observer {
            setCaptureButtonImage(it)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model.showButton(false)

    }

    override fun onResume() {
        super.onResume()

    }

    private fun setupUI() {
        camera_capture_button.setOnClickListener {
            takePhoto()
        }

        video_capture_button.setOnClickListener {
            takeVideo()
        }

        capture_mode_button.setOnClickListener {
            when(cameraModel.showCameraButton.value) {
                CameraModes.PHOTO.mode -> {
                    cameraModel.cameraMode(CameraModes.VIDEO.mode)
                    cameraModel.showPicButton(false)
                }
                CameraModes.VIDEO.mode -> {
                    cameraModel.cameraMode(CameraModes.PHOTO.mode)
                    cameraModel.showPicButton(true)
                }
            }
        }

        switch_cameras_button.setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            bindCameraUseCases()
        }
    }

    private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

            cameraProviderFuture.addListener(Runnable {
                cameraProvider = cameraProviderFuture.get()

                bindCameraUseCases()

            }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {

        val imageCapture = imageCapture ?: return

        val photoFile = File(
                outputDirectory,
                SimpleDateFormat(FILENAME, Locale.US
                ).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

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

                //cameraModel.saveImageLocationToDb()
                insertImageData(savedUri)
                // TODO: add path to db
            }
        })
    }

    private fun takeVideo() {
        // TODO: coming soon
        Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_LONG).show()
    }


    private fun bindCameraUseCases() {
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = viewFinder.display.rotation
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        // Values returned from our analyzer are passed to the attached listener
                        // We log image analysis results here - you should do something useful
                        // instead!
                       // Log.d(TAG, "Average luminosity: $luma")
                    })
                }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)
            preview?.setSurfaceProvider(finder.createSurfaceProvider())
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        // TODO:
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()
            val data = ByteArray(remaining())
            get(data)
            return data
        }

        override fun analyze(image: ImageProxy) {

            if (listeners.isEmpty()) {
                image.close()
                return
            }

            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            lastAnalyzedTimestamp = frameTimestamps.first

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listeners.forEach { it(luma) }

            image.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun setCaptureModeButtonImage(mode: String) {
        when(mode) {
            CameraModes.VIDEO.mode -> capture_mode_button.setImageResource(R.drawable.ic_baseline_videocam_24)
            CameraModes.PHOTO.mode -> capture_mode_button.setImageResource(R.drawable.ic_baseline_camera_24)
        }
    }

    private fun setCaptureButtonImage(picMode: Boolean) {
        when(picMode) {
            true -> {
                camera_capture_button.visibility = View.VISIBLE
                video_capture_button.visibility = View.INVISIBLE
            }
            false -> {
                camera_capture_button.visibility = View.INVISIBLE
                video_capture_button.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    fun insertImageData(image: Uri) {

        val entity = ImageEntity(userName = "MR L Wayne McCarty", fileName = image.toString(), localFilePath = "fake",
        fileExtension = "myext", latitude = 52262255, longitude = 855588445, time = System.currentTimeMillis(), privateImage = false)

        cameraModel.saveImageLocationToDb(entity)
    }
}