package com.hazem.popularpeople.ui.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.hazem.popularpeople.data.PersonImages
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.ui.home.PERSON_ID
import com.hazem.popularpeople.ui.home.PERSON_NAME
import com.hazem.popularpeople.ui.image.ImageFullDisplay
import com.hazem.popularpeople.util.calculateNoOfColumns
import kotlinx.android.synthetic.main.activity_details.*
import androidx.core.app.ActivityOptionsCompat
import com.hazem.popularpeople.R



const val PERSON_IMAGE_PATH = "PERSON_IMAGE_PATH"
class DetailsActivity : AppCompatActivity(), ImageDisplayNavigation {

    private lateinit var viewModel: DetailsViewModel
    private var detailsAdapter = DetailsListAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        title = intent.getStringExtra(PERSON_NAME)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.images.observe(this, Observer {} )
        viewModel.header.observe(this, Observer {
            if (it?.status ==  Resource.Status.SUCCESS){
                detailsAdapter.insertDetails(viewModel.detailsList)
            }
        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        detailsList.adapter = detailsAdapter
        val layoutManager = GridLayoutManager(this,calculateNoOfColumns(this, 170f))
        layoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (position == 0){
                    layoutManager.spanCount
                }else{
                    1
                }
            }

        }
        detailsList.layoutManager = layoutManager
        viewModel.getPersonDetails(intent.getIntExtra(PERSON_ID, 0))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun navigateToDetails(profile: PersonImages.Profile, imageView : View) {
        val intent = Intent(this, ImageFullDisplay::class.java).apply {
            putExtra(PERSON_IMAGE_PATH, profile.filePath)
            putExtra(PERSON_NAME, intent.getStringExtra(PERSON_NAME))
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, resources.getString(R.string.imageTransition))
        startActivity(intent, options.toBundle())
    }

}
