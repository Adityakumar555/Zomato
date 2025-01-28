package com.test.zomato.view.main.home.models.orderModels

data class OrderWithFoodItems(
    val order: OrderDetails,
    val foodItems: List<FoodItemInOrder>
)
