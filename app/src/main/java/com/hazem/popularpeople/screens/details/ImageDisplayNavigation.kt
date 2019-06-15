package com.hazem.popularpeople.screens.details

import android.view.View
import com.hazem.popularpeople.screens.details.data.PersonImages


interface ImageDisplayNavigation {
    fun navigateToDetails(profile: PersonImages.Profile, imageView : View )

}