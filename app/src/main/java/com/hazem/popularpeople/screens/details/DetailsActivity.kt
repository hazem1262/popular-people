package com.hazem.popularpeople.screens.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.hazem.popularpeople.screens.details.data.PersonImages
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.screens.home.PERSON_ID
import com.hazem.popularpeople.screens.home.PERSON_NAME
import com.hazem.popularpeople.screens.image.ImageFullDisplay
import com.hazem.popularpeople.util.calculateNoOfColumns
import kotlinx.android.synthetic.main.activity_details.*
import androidx.core.app.ActivityOptionsCompat
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.BaseActivity
import com.hazem.popularpeople.util.showSkeleton


const val PERSON_IMAGE_PATH = "PERSON_IMAGE_PATH"
const val IMAGE_WIDTH       = "IMAGE_WIDTH"
const val IMAGE_HEIGHT      = "IMAGE_HEIGHT"
class DetailsActivity : BaseActivity(), ImageDisplayNavigation {

    private lateinit var viewModel: DetailsViewModel
    private var detailsAdapter = DetailsListAdapter(this)
    private lateinit var skeleton: RecyclerViewSkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        title = intent.getStringExtra(PERSON_NAME)
        // initialize the view model
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        // observe to the result from search or list data
        registerObservers()

        getData()

        attachLayoutManager()
    }

    private fun registerObservers(){
        viewModel.images.observe(this, Observer {} )
        viewModel.header.observe(this, Observer {
            if (it?.status ==  Resource.Status.SUCCESS){
                skeleton.hide()
                detailsList.adapter = detailsAdapter
                detailsAdapter.insertDetails(viewModel.detailsList)
            }
        })
    }

    @SuppressLint("ResourceType")
    private fun getData(){
        viewModel.getPersonDetails(intent.getIntExtra(PERSON_ID, 0))
        skeleton = detailsList.showSkeleton(R.layout.skeleton_card_details, R.color.white, 10)
        skeleton.show()
    }

    private fun attachLayoutManager(){
        val layoutManager = GridLayoutManager(this,calculateNoOfColumns(this, 170f))
        // to make the details cell take all the row
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
    }
    override fun navigateToDetails(profile: PersonImages.Profile, imageView : View) {
        val intent = Intent(this, ImageFullDisplay::class.java).apply {
            putExtra(PERSON_IMAGE_PATH, profile.filePath)
            putExtra(IMAGE_WIDTH, profile.width)
            putExtra(IMAGE_HEIGHT, profile.height)
            putExtra(PERSON_NAME, intent.getStringExtra(PERSON_NAME))
        }
        // handle shared element transition
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, resources.getString(R.string.imageTransition))
        startActivity(intent, options.toBundle())
    }

}
