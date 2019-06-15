package com.hazem.popularpeople.ui.details

import android.view.View
import com.hazem.popularpeople.data.PersonImages


interface ImageDisplayNavigation {
    fun navigateToDetails(profile: PersonImages.Profile, imageView : View)

}