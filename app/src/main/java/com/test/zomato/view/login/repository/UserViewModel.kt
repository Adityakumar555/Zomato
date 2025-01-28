package com.test.zomato.view.login.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.zomato.repository.roomDb.CartDatabase
import com.test.zomato.view.login.userData.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository by lazy {
        UserRepository(CartDatabase.getInstance(application)!!.userDao())
    }

    // LiveData to observe the user
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    // Fetch user by phone number and update LiveData
    fun getUserByPhoneNumber(userNumber: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByPhoneNumber(userNumber)
            // Update LiveData with the user data (existing user or null)
            _userLiveData.postValue(user)
        }
    }

    // Function to save user data if it doesn't exist already
    fun saveUserData(user: User) {
        viewModelScope.launch {
            // First, check if the user already exists in the database
            val existingUser = userRepository.getUserByPhoneNumber(user.userNumber ?: "")

            // If the user doesn't exist, save the new user
            if (existingUser == null) {
                userRepository.saveUser(user)
            } else {
                // Optionally, you can update the existing user if necessary
                // For now, we just log or ignore as we are not updating in this case.
                _userLiveData.postValue(existingUser)
            }
        }
    }
}
