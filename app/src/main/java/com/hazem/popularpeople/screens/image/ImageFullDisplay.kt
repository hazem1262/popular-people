package com.hazem.popularpeople.screens.image

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.hazem.popularpeople.screens.details.PERSON_IMAGE_PATH
import com.hazem.popularpeople.screens.home.PERSON_NAME
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_full_display.*
import android.graphics.Bitmap
import android.content.Context
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import android.graphics.drawable.Drawable
import android.os.Environment
import com.hazem.popularpeople.R
import com.hazem.popularpeople.util.downloadFile
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Target
import java.io.File
import java.lang.Exception
import kotlin.annotation.Target as Target1


class ImageFullDisplay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_display)
        title = intent.getStringExtra(PERSON_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        Picasso.get().load("http://image.tmdb.org/t/p/w400${intent.getStringExtra(PERSON_IMAGE_PATH)}").fit().into(imageFullDisplay)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.download_image_menu, menu)
        val downLoadImage = menu?.findItem(R.id.downloadBtn)
        downLoadImage?.setOnMenuItemClickListener {
            downLoadImage()
            return@setOnMenuItemClickListener true
        }
        return true
    }
    private fun downLoadImage(){
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Toast.makeText(this@ImageFullDisplay, "downloadingImage", Toast.LENGTH_SHORT).show()
                    downloadFile(context = this@ImageFullDisplay, url = "http://image.tmdb.org/t/p/w400${intent.getStringExtra(PERSON_IMAGE_PATH)}", fileName = intent.getStringExtra(PERSON_IMAGE_PATH))
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@ImageFullDisplay, resources.getString(R.string.storagePermission), Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: PermissionRequest?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }
}
