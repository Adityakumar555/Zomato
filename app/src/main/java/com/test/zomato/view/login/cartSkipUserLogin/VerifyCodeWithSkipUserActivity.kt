package com.test.zomato.view.login.cartSkipUserLogin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.test.zomato.R
import com.test.zomato.databinding.ActivityVerifyCodeWithSkipUserBinding
import com.test.zomato.utils.AppSharedPreferences

class VerifyCodeWithSkipUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyCodeWithSkipUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Uncomment this if you want edge-to-edge UI
        binding = ActivityVerifyCodeWithSkipUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#F3F4FA")

        val mobileNumber = intent?.getStringExtra("skipUserNumber") ?: ""
        val skipUserName = intent?.getStringExtra("skipUserName") ?: ""


        val appSharedPreferences = AppSharedPreferences(this)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.editPhoneNumber.setOnClickListener {
            val intent = Intent(this, MobileNumberLoginWithSkipActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.enterOTP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val otpText = p0.toString().trim()
                if (otpText.length == 4 && otpText == "0000") {
                    appSharedPreferences.saveString("skipUserNumber", mobileNumber)
                    appSharedPreferences.saveString("skipUserName", skipUserName)
                    finish()
                }
            }
        })



        binding.resendOtpTime.setOnClickListener {
            startResendTimer()
        }

        startResendTimer()

    }

    private fun startResendTimer() {
        object : CountDownTimer(30000, 1000) { // 30 seconds timer with 1 second interval
            override fun onTick(millisUntilFinished: Long) {
                binding.resendOtpTime.text = "Resend in ${millisUntilFinished / 1000} sec"
            }

            override fun onFinish() {
                binding.resendOtpTime.text = "Resend OTP"
                binding.resendOtpTime.isEnabled = true
                // binding.resendOtpTime.setBackgroundColor(Color.parseColor("#FF0000"))
            }
        }.start()

        binding.resendOtpTime.isEnabled = false
        //  binding.resendOtpTime.setBackgroundColor(Color.parseColor("#BDBDBD"))
    }
}
