package com.test.zomato.view.main.home.interfaces

import com.test.zomato.view.main.home.models.RestaurantDetails

interface RestaurantsClickListener {
    fun onRestaurantsClick(restaurantDetails: RestaurantDetails)
}