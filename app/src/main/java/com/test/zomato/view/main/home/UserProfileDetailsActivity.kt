package com.test.zomato.view.main.home

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.zomato.R
import com.test.zomato.databinding.ActivityUserProfileDetailsBinding
import com.test.zomato.view.login.userData.User
import com.test.zomato.view.login.repository.UserViewModel

class UserProfileDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileDetailsBinding
    private val userViewModel: UserViewModel by viewModels()

    // Retrieve user phone number from SharedPreferences (or Intent)
    private val userPhoneNumber: String by lazy {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        sharedPreferences.getString("userNumber", "") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewBinding
        binding = ActivityUserProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#F3F4FA")

        // Set up the gender dropdown
        val items = listOf("Male", "Female", "Other", "Prefer not to disclose")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.autoCompleteTextView as AutoCompleteTextView).setAdapter(adapter)

        // Fetch the user data by phone number
        fetchUserData(userPhoneNumber)

        // Update profile button click listener
        binding.updateProfile.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val mobile = binding.mobileInput.text.toString()
            val email = binding.emailInput.text.toString()
            val dob = binding.dobInput.text.toString()
            val anniversary = binding.anniversaryInput.text.toString()
            val gender = binding.autoCompleteTextView.text.toString()

            if (name.isNotEmpty() && mobile.isNotEmpty()) {
                // Create a User object with the updated details
                val user = User(
                    userNumber = mobile,
                    username = name,
                    userEmail = email,
                    userDOB = dob,
                    anniversaryDate = anniversary,
                    gender = gender,
                    imageUrl = null  // Set this based on actual data
                )

                userViewModel.userLiveData.observe(this) { existingUser ->
                    if (existingUser != null) {
                        // User exists, update the existing user's data
                        user.id = existingUser.id  // Retain the existing user's ID
                        updateUserData(user)  // Update the existing user's data
                    } else {
                        // User doesn't exist, save new user data
                        saveUserData(user)  // Save the new user data
                    }
                }
            } else {
                Toast.makeText(this, "Name and Mobile are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to fetch user data based on phone number
    private fun fetchUserData(userPhoneNumber: String) {
        // Check if the phone number is valid before making the request
        if (userPhoneNumber.isNotEmpty()) {
            userViewModel.getUserByPhoneNumber(userPhoneNumber)

            userViewModel.userLiveData.observe(this) { user ->
                if (user != null) {
                    // Populate fields with existing data
                    binding.nameInput.setText(user.username)
                    binding.mobileInput.setText(user.userNumber)
                    binding.emailInput.setText(user.userEmail)
                    binding.dobInput.setText(user.userDOB)
                    binding.anniversaryInput.setText(user.anniversaryDate)
                    binding.autoCompleteTextView.setText(user.gender)
                } else {
                    // If the user does not exist, show a message or leave fields empty
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to save user data in the database
    private fun saveUserData(user: User) {
        userViewModel.saveUserData(user)
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
    }

    // Function to update user data in the database
    private fun updateUserData(user: User) {
        userViewModel.saveUserData(user)
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
    }
}
