package com.test.zomato.view.location

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.test.zomato.databinding.ActivitySetLocationBinding
import com.test.zomato.view.main.MainActivity
import com.test.zomato.utils.EnableAppLocationPermissionDialogFragment
import com.test.zomato.utils.MyHelper
import com.test.zomato.utils.RequestPermissionDialog

class SetLocationPermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetLocationBinding
    private val myHelper: MyHelper by lazy { MyHelper(this) }
    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSharedPreferences("AppPreferences", MODE_PRIVATE).edit()
            .putBoolean("VisitedSetLocation", true)
            .apply()

        // Set up result launcher for GPS settings
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    navigateToMainActivity()
                } else {
                    navigateToMainActivity()
                }
            }

        if (myHelper.checkPermission()) {
            if (myHelper.isLocationEnable()) {
                navigateToMainActivity()
            }
        }


        binding.enableDeviceLocation.setOnClickListener {
            if (myHelper.checkPermission()) {
                if (!myHelper.isLocationEnable()) {
                    myHelper.onGPS(resultLauncher)
                } else {
                    navigateToMainActivity()
                }
            } else {
                myHelper.requestLocationPermission(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (myHelper.isLocationEnable()) {
                    navigateToMainActivity()
                } else {
                    myHelper.onGPS(resultLauncher)
                }
            } else {
                //  true: The user denied the permission without selecting "Don't ask again."
                //  false: The user denied the permission and selected "Don't ask again."
                if (permissions.isNotEmpty()) {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        // User selected "Don't Ask Again"
                        val progressDialog = EnableAppLocationPermissionDialogFragment()
                        progressDialog.show(supportFragmentManager, "requestLocationPermission")
                    } else {
                        // Permission denied without "Don't Ask Again"
                        val progressDialog = RequestPermissionDialog()
                        progressDialog.show(supportFragmentManager, "requestPermissionDialog")

                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
        finish()
    }
}
