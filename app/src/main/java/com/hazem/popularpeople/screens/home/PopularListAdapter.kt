package com.hazem.popularpeople.screens.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.abdularis.civ.AvatarImageView
import com.hazem.popularpeople.R
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.util.getImageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_person.view.*

class PopularListAdapter(var detailsNavigation: DetailsNavigation) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var persons = ArrayList<PopularPersons.PopularPerson>()

    fun insertPersons(popularPersons : List<PopularPersons.PopularPerson>){
        persons.clear()
        persons.addAll(popularPersons)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PopularPersonsViewHolder(view)
    }

    override fun getItemCount(): Int  = persons.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularPersonsViewHolder).bindPerson(persons[position])
    }

    inner class PopularPersonsViewHolder(var v:View):RecyclerView.ViewHolder(v){
        fun bindPerson(person: PopularPersons.PopularPerson){

            // load person image
            if (!person.profilePath.isNullOrEmpty()){
                v.personImg.state = AvatarImageView.SHOW_IMAGE
                Picasso.get().load(person.profilePath.getImageUrl()).fit().into(v.personImg)
            }else{
                v.personImg.state = AvatarImageView.SHOW_INITIAL
                v.personImg.setText(person.name)
            }
            v.personName.text       = person.name
            v.personPopularity.text = person.popularity.toString()
            var knownFor = ""
            person.knownFor?.forEach {
                knownFor += if (it?.originalTitle != null){
                    "${it?.originalTitle}, "
                }else {
                    "${it?.originalName}, "
                }
            }
            v.personKnownFor.text = knownFor

            v.setOnClickListener { detailsNavigation.navigateToDetails(person) }
            v.personImg.setOnClickListener { detailsNavigation.navigateToDetails(person) }
        }
    }
}