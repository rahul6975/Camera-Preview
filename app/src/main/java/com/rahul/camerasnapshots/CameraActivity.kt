package com.rahul.camerasnapshots

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.rahul.camerasnapshots.repository.MyRepository
import com.rahul.camerasnapshots.room.EntityClass
import com.rahul.camerasnapshots.viewModel.MyViewModel
import com.rahul.camerasnapshots.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CameraActivity : AppCompatActivity() {
    private var camera: Camera? = null
    private var preview: Preview? = null
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    var imageName = ""
    var albumName = ""
    lateinit var myApplication: MyApplication
    lateinit var myRepository: MyRepository

    private var imageCapture: ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        initViewsAndListeners()
        startCamera()
    }

    private fun initViewsAndListeners() {
        myApplication = application as MyApplication

        myRepository = myApplication.myRepository

        viewModelFactory = ViewModelFactory(myRepository)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)

        btnClick.setOnClickListener {
            takePhotos()
        }
    }

    private fun takePhotos() {
        //save photos
        imageName = "image-${System.currentTimeMillis()}.jpg"
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            imageName
        )
        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(applicationContext, "Image saved", Toast.LENGTH_SHORT).show()
                    showDialog()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        "An error occurred while saving",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    fun showDialog() {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Album Name")

        val current_time = Calendar.getInstance().time
        // Set up the input
        val input = EditText(this)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Album Name")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            albumName = input.text.toString()
            val entityClass =
                EntityClass(imageName, current_time.toString(), albumName)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addImage(entityClass)
            }
            finish()
        })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
}