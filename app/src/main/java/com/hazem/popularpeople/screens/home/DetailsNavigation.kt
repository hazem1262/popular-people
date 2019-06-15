package com.hazem.popularpeople.screens.home

import com.hazem.popularpeople.screens.home.data.PopularPersons

interface DetailsNavigation {
    fun navigateToDetails(person: PopularPersons.PopularPerson)
}