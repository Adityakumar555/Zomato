package com.test.zomato.view.main.home.interfaces

import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.main.home.models.RestaurantDetails

interface AddFoodClickListener {
    fun onFoodClick(foodItem: FoodItem)
    fun onFoodQuantityChange(foodItem: FoodItem)  // New method to handle quantity change
}
