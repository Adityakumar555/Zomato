package com.test.zomato.view.login.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.zomato.view.login.userData.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE userNumber = :userNumber")
    suspend fun getUserByPhoneNumber(userNumber: String): User?
}
