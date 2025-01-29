package com.test.zomato.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.zomato.repository.GeoApifyRepository
import com.test.zomato.repository.roomDb.CartDatabase
import com.test.zomato.repository.roomDb.RoomDbRepository
import com.test.zomato.view.location.models.PlaceFeature
import com.test.zomato.view.location.models.UserSavedAddress
import com.test.zomato.view.location.repository.UserSavedAddressDao
import com.test.zomato.view.location.repository.UserSavedAddressRepository
import com.test.zomato.view.login.repository.UserRepository
import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.main.home.models.RestaurantDetails
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userSavedAddressRepository by lazy {
        UserSavedAddressRepository(CartDatabase.getInstance(application)!!.userSavedAddressDao())
    }

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



    private val setLocationList = MutableLiveData<List<PlaceFeature>>()
    val nearLocationList: LiveData<List<PlaceFeature>> = setLocationList

    private val geoApifyRepository by lazy { GeoApifyRepository() }

    fun getNearByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val list = geoApifyRepository.fetchNearbyLocation(latitude, longitude)
            setLocationList.postValue(list)
        }
    }


    // Function to save user address
    fun saveAddress(
        receiverName: String,
        receiverNumber: String,
        saveAddressAs: String,
        selectedLocation: String,
        houseAddress: String,
        nearbyLandmark: String
    ) {
        viewModelScope.launch {
            val address = UserSavedAddress(
                receiverName = receiverName,
                receiverNumber = receiverNumber,
                saveAddressAs = saveAddressAs,
                selectedLocation = selectedLocation,
                houseAddress = houseAddress,
                nearbyLandmark = nearbyLandmark
            )
            userSavedAddressRepository.saveAddress(address)
        }
    }

    fun getAllAddresses() {
        viewModelScope.launch {
            val addresses = userSavedAddressRepository.getAllAddresses()
            // Handle the list of addresses as needed (e.g., updating UI)
        }
    }


}
