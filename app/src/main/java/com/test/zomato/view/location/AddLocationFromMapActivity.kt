package com.test.zomato.view.location

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.zomato.R
import com.test.zomato.databinding.ActivityAddLocationFromMapBinding
import com.test.zomato.utils.MyHelper
import java.io.IOException
import java.util.Locale

class AddLocationFromMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityAddLocationFromMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var markerPosition: LatLng? = null
    private var selectedAddress: String? = null
    private val myHelper by lazy { MyHelper(this) }
    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLocationFromMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission is required to add address.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        window.statusBarColor = Color.parseColor("#F3F4FA")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.backButton.setOnClickListener { finish() }


        binding.userCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }

        // Set up Add More Details button to open bottom sheet and pass the address
        binding.addMoreDetails.setOnClickListener {
            val enterCompleteAddressBottomSheetFragment = EnterCompleteAddressBottomSheetFragment()
            val bundle = Bundle()
            bundle.putString("address", selectedAddress)  // Pass address to the bottom sheet
            enterCompleteAddressBottomSheetFragment.arguments = bundle
            enterCompleteAddressBottomSheetFragment.show(
                supportFragmentManager,
                "enterCompleteAddressBottomSheetFragment"
            )
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        getLocationFromAddress(it.toString())
                    }
                }
            }
        })


        val locationData =
            myHelper.extractAddressDetails(myHelper.getLatitude(), myHelper.getLongitude())
        locationData?.let { updateLocationUI(it.block, it.fullAddress) }

        if (myHelper.checkLocationPermission() && myHelper.isLocationEnable()) {
            getCurrentLocation()
        } else {
            if (!myHelper.isLocationEnable()) {
                myHelper.onGPS(resultLauncher)
            } else {
                myHelper.requestLocationPermission(this)
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        // Set a click listener for the map
        googleMap.setOnMapClickListener { latLng ->
            // Clear existing markers and place a new marker at the clicked location
            googleMap.clear()  // Clear any existing markers
            googleMap.addMarker(MarkerOptions().position(latLng))

          //  markerPosition = latLng

            // Extract address details from the clicked location
            extractAddressDetails(latLng.latitude, latLng.longitude)
        }

    }

    // Function to fetch the address details from the given location
    private fun extractAddressDetails(latitude: Double, longitude: Double) {
        val location = myHelper.extractAddressDetails(latitude, longitude)
        location?.let {
            updateLocationUI(it.block, "${it.locality},${it.state}")

            selectedAddress = "${it.block},${it.locality},${it.state}"
        }

    }

    // Function to update UI with block and full address
    private fun updateLocationUI(block: String, fullAddress: String) {
        binding.userBlockLocation.text = block
        binding.address.text = fullAddress
    }

    // Function to get current location
    private fun getCurrentLocation() {
        if (myHelper.checkLocationPermission() && myHelper.isLocationEnable()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)

                    // Move camera to current location and set a marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(currentLatLng))

                    extractAddressDetails(currentLatLng.latitude, currentLatLng.longitude)
                }
            }
        } else {
            myHelper.requestLocationPermission(this)
        }
    }

    private fun getLocationFromAddress(address: String) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 1)

            if (!addresses.isNullOrEmpty()) {
                val location = addresses[0]
                val latLng = LatLng(location.latitude, location.longitude)

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng))

                // Extract and display address details for the new location
                extractAddressDetails(latLng.latitude, latLng.longitude)

            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Unable to find the location.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!myHelper.isLocationEnable()) {
                    myHelper.onGPS(resultLauncher)
                } else {
                    getCurrentLocation()
                }

            } else {
                Toast.makeText(this, "Location permission is required to get current location.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
