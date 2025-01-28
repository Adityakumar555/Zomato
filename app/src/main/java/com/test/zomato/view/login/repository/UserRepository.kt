package com.test.zomato.view.login.repository

import com.test.zomato.view.login.userData.User

class UserRepository(private val userDao: UserDao) {

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }

    suspend fun getUserByPhoneNumber(userNumber: String): User? {
        return userDao.getUserByPhoneNumber(userNumber)
    }
}
