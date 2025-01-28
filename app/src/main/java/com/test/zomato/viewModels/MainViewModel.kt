package com.test.zomato.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.zomato.repository.GeoApifyRepository
import com.test.zomato.repository.roomDb.RoomDbRepository
import com.test.zomato.view.location.models.PlaceFeature
import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.main.home.models.RestaurantDetails
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private val setCount = MutableLiveData<Int>(1)
    val count: LiveData<Int> = setCount

    fun plus() {
        setCount.value = (setCount.value ?: 0) + 1
    }

    fun minus() {
        val currentValue = setCount.value ?: 0
        if (currentValue >= 1) {
            setCount.value = currentValue - 1
        }
    }

    // Set the initial count value to the passed quantity
    fun setInitialQuantity(quantity: Int) {
        setCount.value = quantity
    }


    private val setLocationList = MutableLiveData<List<PlaceFeature>>()
    val nearLocationList: LiveData<List<PlaceFeature>> = setLocationList

    private val geoApifyRepository by lazy { GeoApifyRepository() }

    fun getNearByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val list = geoApifyRepository.fetchNearbyLocation(latitude, longitude)
            setLocationList.postValue(list)
        }
    }


    fun saveLocation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }





}
