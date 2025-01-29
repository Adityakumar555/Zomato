package com.test.zomato.view.location.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.test.zomato.view.location.models.UserSavedAddress

@Dao
interface UserSavedAddressDao {

    @Insert
    suspend fun insertAddress(address: UserSavedAddress)

    @Query("SELECT * FROM user_saved_address")
    suspend fun getAllAddresses(): List<UserSavedAddress>

    @Delete
    suspend fun deleteAddress(address: UserSavedAddress)
}
