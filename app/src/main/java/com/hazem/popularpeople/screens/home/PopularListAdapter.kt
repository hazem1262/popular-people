package com.hazem.popularpeople.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.abdularis.civ.AvatarImageView
import com.hazem.popularpeople.R
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.util.getImageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_person.view.*

class PopularListAdapter(var detailsNavigation: DetailsNavigation) : PagedListAdapter<PopularPersons.PopularPerson, RecyclerView.ViewHolder>(diffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PopularPersonsViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularPersonsViewHolder).bindPerson(getItem(position)!!)
    }

    inner class PopularPersonsViewHolder(var v:View):RecyclerView.ViewHolder(v){
        fun bindPerson(person: PopularPersons.PopularPerson){
            // load person image
            if (!person.profilePath.isNullOrEmpty()){
                v.personImg.state = AvatarImageView.SHOW_IMAGE
                Picasso.get()
                    .load(person.profilePath.getImageUrl())
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into(v.personImg)
            }else{
                v.personImg.state = AvatarImageView.SHOW_INITIAL
                v.personImg.setText(person.name)
            }
            v.personName.text       = person.name
            if (person.popularity == null){
                v.personPopularityHeader.visibility = View.GONE
                v.personPopularity.visibility       = View.GONE
            }else{
                v.personPopularity.text = person.popularity.toString()
            }
            if (person.knownFor.isNullOrEmpty()){
                v.personKnownFor.visibility       = View.GONE
                v.personKnownForHeader.visibility = View.GONE

            }else{
                var knownFor = ""
                person.knownFor?.forEach {
                    knownFor += if (it?.originalTitle != null){
                        "${it?.originalTitle}, "
                    }else {
                        "${it?.originalName}, "
                    }
                }
                v.personKnownFor.text = knownFor.substring(0, knownFor.length - 2)   //remove last ,
            }

            v.setOnClickListener { detailsNavigation.navigateToDetails(person) }
            v.personImg.setOnClickListener { detailsNavigation.navigateToDetails(person) }
        }
    }
    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<PopularPersons.PopularPerson>() {
            override fun areItemsTheSame(oldItem: PopularPersons.PopularPerson, newItem: PopularPersons.PopularPerson): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PopularPersons.PopularPerson, newItem: PopularPersons.PopularPerson): Boolean =
                oldItem == newItem
        }
    }
}