package com.hazem.popularpeople.ui.home

import com.hazem.popularpeople.data.PopularPersons

interface DetailsNavigation {
    fun navigateToDetails(person: PopularPersons.PopularPerson)
}