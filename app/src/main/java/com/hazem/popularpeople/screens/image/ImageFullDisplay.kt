package com.hazem.popularpeople.screens.image

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
            Toast.makeText(this, "downloadingImage", Toast.LENGTH_SHORT).show()
            downloadFile(context = this@ImageFullDisplay, url = "http://image.tmdb.org/t/p/w400${intent.getStringExtra(PERSON_IMAGE_PATH)}", fileName = intent.getStringExtra(PERSON_IMAGE_PATH))
            return@setOnMenuItemClickListener true
        }
        return true
    }

    //target to save
    private fun getTarget(url: String): Target {
        return object : Target {
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                Thread(Runnable {
                    val file = File(Environment.getExternalStorageDirectory().path  + url)
                    try {
                        file.createNewFile()
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                        ostream.flush()
                        ostream.close()
                    } catch (e: IOException) {
                    }
                }).start()

            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }
        }
    }
}
