package com.rahul.camerasnapshots.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahul.camerasnapshots.R
import com.rahul.camerasnapshots.adapter.ImageAdapter
import com.rahul.camerasnapshots.clickInterface.ClickListener
import com.rahul.camerasnapshots.repository.MyRepository
import com.rahul.camerasnapshots.room.EntityClass
import com.rahul.camerasnapshots.viewModel.MyViewModel
import com.rahul.camerasnapshots.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ClickListener {

    private lateinit var myApplication: MyApplication
    private lateinit var myRepository: MyRepository
    private lateinit var viewModel: MyViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private var imageList = emptyList<EntityClass>()
    private lateinit var imageAdapter: ImageAdapter
    private val CAMERA_REQESUT_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        initViewsAndListeners()

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = imageAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.displayImage().observe(this, Observer {
            progressBar.visibility = View.GONE
            imageAdapter.updateList(it)
        })

    }

    /*
    below function initialize the variables
     */
    private fun initViewsAndListeners() {
        myApplication = application as MyApplication
        myRepository = myApplication.myRepository

        imageList = arrayListOf<EntityClass>()

        imageAdapter = ImageAdapter(imageList, this)

        viewModelFactory = ViewModelFactory(myRepository)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MyViewModel::class.java)

        addImage.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

    }


    /*
         below function checks the required permissions and asks the same
         to user if not granted yet
   */
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                CAMERA_REQESUT_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {

        } else {
            Toast.makeText(
                this,
                "Permissions are denied, please allow camera permission from settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*
       below function is called everytime any view is clicked in the recyclerview
   */
    override fun onClick(position: Int) {
        var uri = ""
        viewModel.displayImage().observe(this, Observer {
            uri = it[position].path
            val intent = Intent(this, ImageDetails::class.java)
            intent.putExtra("uri", uri)
            startActivity(intent)
        })
    }
}