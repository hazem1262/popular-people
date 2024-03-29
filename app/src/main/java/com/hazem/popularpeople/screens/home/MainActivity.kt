package com.hazem.popularpeople.screens.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.hazem.popularpeople.R
import com.hazem.popularpeople.core.ui.BaseActivity
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.screens.details.DetailsActivity
import com.hazem.popularpeople.screens.home.data.DataType
import com.hazem.popularpeople.util.showSkeleton

const val PERSON_ID    = "personID"
const val PERSON_NAME  = "personName"
const val FROM_STARRED = "FROM_STARRED"
/*
* this activity will be responsible for display [List - Search - Star] people
* intent.getBooleanExtra(FROM_STARRED) this check to know if it is from star btn
* */
class MainActivity : BaseActivity<HomeViewModel>(), DetailsNavigation{

    private var personsAdapter : PopularListAdapter = PopularListAdapter(this)
    private lateinit var skeleton: RecyclerViewSkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerObservers()
        // hide the back btn in the tool bar if not in stars screen
        if (!intent.getBooleanExtra(FROM_STARRED, false)){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }else {
            title = resources.getString(R.string.topRated)
            viewModel.apiHelper.dataType = DataType.Star
        }

        // register the poll to refresh layout
        reloadData()
    }

    private fun registerObservers() {
        viewModel.liveData?.observe(this, Observer {
            popularList.adapter = personsAdapter
            personsAdapter.submitList(it!!)
        })
    }


    private fun reloadData() {
        refreshLayout.setOnRefreshListener {
            // only refresh data in case of Browse
            if (viewModel.apiHelper.dataType == DataType.Browse){
                viewModel.resetObservable(viewModel.apiHelper.dataType, forceReset = true)
            }
            refreshLayout.isRefreshing = false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        do not show menu items in starred screen
        if (!intent.getBooleanExtra(FROM_STARRED, false)){
            menuInflater.inflate(R.menu.popular_search_menu, menu)

            val starItem = menu?.findItem(R.id.starItem)
            starItem?.setOnMenuItemClickListener {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(FROM_STARRED, true)
                }
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
            val searchItem = menu?.findItem(R.id.actionSearch)
            val searchView = searchItem?.actionView as SearchView
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener{

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.resetObservable(dataType = DataType.Search, searchQuery = query)
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
        }

        return true
    }

    override fun showLoading() {
        skeleton = popularList.showSkeleton(R.layout.skeleton_card_home, R.color.white, 10)
        skeleton.show()
    }

    override fun hideLoading() {
        skeleton.hide()
        popularList.adapter = personsAdapter
    }
    override fun navigateToDetails(person: PopularPersons.PopularPerson){
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(PERSON_ID, person.id)
            putExtra(PERSON_NAME, person.name)
        }
        startActivity(intent)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        super.onNetworkConnectionChanged(isConnected)
        viewModel.isConnected = isConnected
    }
}
