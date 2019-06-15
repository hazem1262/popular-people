package com.hazem.popularpeople.screens.home

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hazem.popularpeople.core.Resource
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.BaseActivity
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.screens.details.DetailsActivity
import com.hazem.popularpeople.screens.home.data.DataType
import com.hazem.popularpeople.util.showSkeleton

const val PERSON_ID   = "personID"
const val PERSON_NAME = "personName"
class MainActivity : BaseActivity(), DetailsNavigation{

    private var personsAdapter : PopularListAdapter =
        PopularListAdapter(this)
    private lateinit var viewModel: HomeViewModel
    private lateinit var skeleton: RecyclerViewSkeletonScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.popularPersons?.observe(this, Observer {
            if (viewModel.networkHelper.currentPage <= 2){
                skeleton.hide()
                popularList.adapter = personsAdapter
            }
            if (it?.status == Resource.Status.SUCCESS){
                personsAdapter.insertPersons(it.data!!)
            }
        })
        viewModel.getPopularPersons()
        skeleton = popularList.showSkeleton(R.layout.skeleton_card_home, R.color.white, 10)

        popularList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.getData()
                    }
                }
            }
        )

        // load search query after configuration change
        if(viewModel.networkHelper.searchQuery.isNotEmpty()){
            invalidateOptionsMenu()
        }

        // hide the back btn in the tool bar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.popular_search_menu, menu)

        val searchItem = menu?.findItem(R.id.actionSearch)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.resetObservable(dataType = DataType.Search, searchQuery = query)
                    viewModel.getData()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.isNotEmpty() == true){
                        viewModel.updateSearchQuery(newText)
                    }
                    return false
                }

            }
        )

        searchItem.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.resetObservable(dataType = DataType.Browse)
                    return true
                }

            }
        )
        return true
    }

    override fun navigateToDetails(person: PopularPersons.PopularPerson){
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(PERSON_ID, person.id)
            putExtra(PERSON_NAME, person.name)
        }
        startActivity(intent)
    }
}
