package com.test.zomato.view.location.repository

import com.test.zomato.view.location.models.UserSavedAddress

class UserSavedAddressRepository(private val userSavedAddressDao: UserSavedAddressDao) {

    suspend fun saveAddress(userSavedAddress: UserSavedAddress) {
        userSavedAddressDao.insertAddress(userSavedAddress)
    }

    suspend fun getAllAddresses(numberIs: String): List<UserSavedAddress> {
        return userSavedAddressDao.getAllAddresses(numberIs)
    }

    suspend fun deleteAddress(userSavedAddress: UserSavedAddress){
        return userSavedAddressDao.deleteAddress(userSavedAddress)
    }

    suspend fun updateAddress(userSavedAddress: UserSavedAddress){
        return userSavedAddressDao.updateAddress(userSavedAddress)
    }

    suspend fun deleteAddressesWithUserNumber(currentUserNumber: String) {
        userSavedAddressDao.deleteAddressesWithUserNumber(currentUserNumber)
    }

    suspend fun saveMultipleAddresses(addresses: List<UserSavedAddress>) {
        userSavedAddressDao.insertMultipleAddresses(addresses)
    }

}
