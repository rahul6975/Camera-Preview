package com.rahul.camerasnapshots

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image_details.*

class ImageDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        val url = intent.getStringExtra("uri")
        val uri = Uri.parse(url)
        imgDisplayImage.setImageURI(uri)
    }
}