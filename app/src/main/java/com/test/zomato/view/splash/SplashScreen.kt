package com.test.zomato.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.zomato.R
import com.test.zomato.utils.AppSharedPreferences
import com.test.zomato.view.location.SetLocationPermissionActivity
import com.test.zomato.view.login.UserSignUpActivity
import com.test.zomato.view.main.MainActivity
import com.test.zomato.utils.MyHelper

class SplashScreen : AppCompatActivity() {

    private lateinit var myHelper: MyHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.splash_screen)

        myHelper = MyHelper(this)
        myHelper.setStatusBarIconColor(this,true)


        val appPreferences = AppSharedPreferences(this)
        val isSkipBtnClick = appPreferences.getBoolean("skipBtnClick")
        val hasVisitedMainActivity = appPreferences.getBoolean("VisitedMainActivity", false)
        val hasVisitedSetLocation = appPreferences.getBoolean("VisitedSetLocation", false)

        if (isSkipBtnClick) {
            Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, UserSignUpActivity::class.java))
            }, 1000)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    hasVisitedMainActivity -> {
                        // Navigate directly to MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    hasVisitedSetLocation -> {
                        // Navigate directly to SetLocationActivity
                        startActivity(Intent(this, SetLocationPermissionActivity::class.java))
                    }
                    else -> {
                        // Navigate to UserSignUpActivity
                        startActivity(Intent(this, UserSignUpActivity::class.java))
                    }
                }
                finish()
            }, 1000)
        }


    }
}
