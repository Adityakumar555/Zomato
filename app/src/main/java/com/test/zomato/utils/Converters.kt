package com.test.zomato.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.zomato.view.main.home.models.FoodItem

class Converters {

    // TypeConverter for List<String>
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    // TypeConverter for List<FoodItem>
    @TypeConverter
    fun fromFoodItemList(value: List<FoodItem>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toFoodItemList(value: String): List<FoodItem> {
        val listType = object : TypeToken<List<FoodItem>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
