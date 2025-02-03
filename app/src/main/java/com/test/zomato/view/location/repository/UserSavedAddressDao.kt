package com.test.zomato.view.location.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.zomato.view.location.models.UserSavedAddress

@Dao
interface UserSavedAddressDao {

    @Insert
    suspend fun insertAddress(address: UserSavedAddress)

    @Query("SELECT * FROM user_saved_address where currentUserNumber = :numberIs")
    suspend fun getAllAddresses(numberIs: String): List<UserSavedAddress>

    @Update
    suspend fun updateAddress(address: UserSavedAddress)

    @Query("delete from user_saved_address where id=:addressId")
    suspend fun deleteAddress(addressId: Int)

    @Query("DELETE FROM user_saved_address WHERE currentUserNumber = :currentUserNumber")
    suspend fun deleteAddressesWithUserNumber(currentUserNumber: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleAddresses(addresses: List<UserSavedAddress>)


    @Query("UPDATE user_saved_address SET addressSelected = :status WHERE currentUserNumber = :userPhoneNumber")
    suspend fun updateAddressSelectedStatus(userPhoneNumber: String, status: Boolean)



}
