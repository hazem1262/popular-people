package com.hazem.popularpeople.screens.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.popularpeople.R
import com.hazem.popularpeople.screens.details.data.PersonDetails
import com.hazem.popularpeople.screens.details.data.PersonImages
import com.hazem.popularpeople.util.getImageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_image.view.*
import kotlinx.android.synthetic.main.details_person.view.*

class DetailsListAdapter(var imageDisplayNavigation:ImageDisplayNavigation) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var profiles = ArrayList<Any>()

    fun insertDetails(detailsList: List<Any>){
        profiles.clear()
        profiles.addAll(detailsList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == HEADER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.details_person, parent, false)
            DetailsHeaderHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.details_image, parent, false)
            ImagesViewHolder(view)
        }

    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (profiles[position] is PersonDetails){
            (holder as DetailsHeaderHolder).bindDetails(profiles[position] as PersonDetails)
        }else{
            (holder as ImagesViewHolder).bindImage(profiles[position] as PersonImages.Profile)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (profiles[position] is PersonDetails){
            HEADER
        }else{
            BODY
        }
    }

    inner class ImagesViewHolder(var v: View):RecyclerView.ViewHolder(v){
        fun bindImage(image: PersonImages.Profile){
            // check if male or female
            var placeholder = if ((profiles[0] as PersonDetails).gender == 2) R.drawable.male else R.drawable.female
            Picasso.get()
                .load(image.filePath?.getImageUrl())
                .placeholder(placeholder)
                .fit()
                .into(v.detailsImage)
            v.setOnClickListener {
                imageDisplayNavigation.navigateToDetails(image, v.detailsImage)
            }
        }
    }

    inner class DetailsHeaderHolder(var v: View) : RecyclerView.ViewHolder(v){
        fun bindDetails(details: PersonDetails){
            if (details.biography.isNullOrEmpty()){
                v.bioHeader.visibility = View.GONE
            }else{
                v.bio.text = details.biography
            }

            if (details.birthday.isNullOrEmpty()){
                v.birthDayHeader.visibility = View.GONE
            }else{
                v.birthDay.text = details.birthday
            }

            if (details.knownForDepartment.isNullOrEmpty()){
                v.departmentHeader.visibility = View.GONE
            }else{
                v.department.text = details.knownForDepartment
            }
            if (details.gender == 0){
                v.genderHeader.visibility = View.GONE
            }else{
                v.gender.text = if (details.gender == 2) "Male" else "Female"
            }
        }
    }
}
const val HEADER = 1
const val BODY = 2