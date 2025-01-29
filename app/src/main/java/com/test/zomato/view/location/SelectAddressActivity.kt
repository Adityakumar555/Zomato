package com.test.zomato.view.location

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.zomato.R
import com.test.zomato.databinding.ActivitySelectAddressBinding
import com.test.zomato.utils.EnableAppLocationPermissionDialogFragment
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.location.adapter.ViewNearbyLocationsAdapter
import com.test.zomato.viewModels.MainViewModel

class SelectAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectAddressBinding

    private lateinit var mainViewModel: MainViewModel

    private val myHelper: MyHelper by lazy { MyHelper(this) }

    private lateinit var viewNearbyLocationsAdapter: ViewNearbyLocationsAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    recreate()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission is required to find nearby shops",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addAddress.setOnClickListener {
            val intent = Intent(this,AddLocationFromMapActivity::class.java)
            startActivity(intent)
        }

        if (!myHelper.checkLocationPermission() || !myHelper.isLocationEnable()) {
            binding.text1.text = "Device location not\nenabled"
            binding.text5.text = "Tab here to enable your device\nlocation for a better"
            binding.enableLocationBtn.visibility = View.VISIBLE
            binding.selectCurrentLocation.visibility = View.GONE

            binding.enableLocationBtn.setOnClickListener {
                if (myHelper.checkLocationPermission()) {
                    if (!myHelper.isLocationEnable()) {
                        myHelper.onGPS(resultLauncher)
                    }
                } else {
                    myHelper.requestLocationPermission(this)
                }

            }

        } else {

            val locationData =
                myHelper.extractAddressDetails(myHelper.getLatitude(), myHelper.getLongitude())
            binding.text1.text = "Use current location"

            binding.text5.text =
                "${locationData?.block},${locationData?.locality},${locationData?.state}"

            binding.enableLocationBtn.visibility = View.GONE
            binding.selectCurrentLocation.visibility = View.VISIBLE

            binding.selectCurrentLocation.setOnClickListener {
                finish()
            }
        }

        Toast.makeText(
            this,
            "${myHelper.getLatitude()},${myHelper.getLongitude()}",
            Toast.LENGTH_SHORT
        ).show()

        setAdapter()
        updateTheNearbyLocationAdapter()

    }


    private fun setAdapter() {
        viewNearbyLocationsAdapter = ViewNearbyLocationsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = viewNearbyLocationsAdapter
    }


    private fun updateTheNearbyLocationAdapter() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        mainViewModel.getNearByLocation(myHelper.getLatitude(),myHelper.getLongitude())

        mainViewModel.nearLocationList.observe(this, Observer { validPlaces ->
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            viewNearbyLocationsAdapter.updateList(validPlaces as MutableList)
            viewNearbyLocationsAdapter.notifyDataSetChanged()
            //  nearByLocationList.addAll(validPlaces)

            for (place in validPlaces) {
                Log.d(
                    "SelectAddressActivity",
                    "Name: ${place.properties.name}, Address: ${place.properties.formatted}"
                )
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
            myHelper.onGPS(resultLauncher)
        } else {
            if (!shouldShowRequestPermissionRationale(permissions[0])) {
                val progressDialog = EnableAppLocationPermissionDialogFragment()
                progressDialog.show(supportFragmentManager, "requestLocationPermission")
            }
        }
    }

    /*    private fun fetchNearbyShops(latitude: Double, longitude: Double) {
            val call = RetrofitInstance.apiCall.getNearbyPlaces(
                categories = "commercial,education,catering",
                filter = "circle:$longitude,$latitude,5000",
                bias = "proximity:$longitude,$latitude",
                limit = 30,
                apiKey = "8a3768af0f8143a5b82b582da20558c6"
            )

            call.enqueue(object : retrofit2.Callback<PlacesResponse> {
                override fun onResponse(
                    call: Call<PlacesResponse>,
                    response: retrofit2.Response<PlacesResponse>
                ) {
                    if (response.isSuccessful) {
                        val places = response.body()?.features
                        if (!places.isNullOrEmpty()) {
                            val validPlaces = places.filter {
                                !it.properties.name.isNullOrEmpty() && !it.properties.formatted.isNullOrEmpty()
                            }



                            for (place in validPlaces) {
                                Log.d("SelectAddressActivity", "Name: ${place.properties.name}, Address: ${place.properties.formatted}")
                            }

                            Toast.makeText(
                                this@SelectAddressActivity,
                                if (validPlaces.isNotEmpty()) "Shops logged successfully" else "No valid shops found",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.d("SelectAddressActivity", "No shops found nearby")
                            Toast.makeText(this@SelectAddressActivity, "No shops found nearby", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("SelectAddressActivity", "Failed to fetch shops: ${response.errorBody()?.string()}")
                        Toast.makeText(this@SelectAddressActivity, "Failed to fetch shops", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                    Log.e("SelectAddressActivity", "Error: ${t.localizedMessage}")
                    Toast.makeText(this@SelectAddressActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }*/

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
    }
}