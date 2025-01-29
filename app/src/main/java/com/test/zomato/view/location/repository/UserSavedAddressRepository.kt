package com.test.zomato.view.location.repository

import com.test.zomato.view.location.models.UserSavedAddress

class UserSavedAddressRepository(private val userSavedAddressDao: UserSavedAddressDao) {

    suspend fun saveAddress(userSavedAddress: UserSavedAddress) {
        userSavedAddressDao.insertAddress(userSavedAddress)
    }

    suspend fun getAllAddresses(): List<UserSavedAddress> {
        return userSavedAddressDao.getAllAddresses()
    }
}
