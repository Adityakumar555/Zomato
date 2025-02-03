package com.test.zomato.view.location

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.zomato.R
import com.test.zomato.databinding.ActivityMyAddressesBinding
import com.test.zomato.utils.AppSharedPreferences
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.location.adapter.ShowAllSavedAddressAdapter
import com.test.zomato.view.location.interfaces.AddressMenuClickListener
import com.test.zomato.view.location.models.UserSavedAddress
import com.test.zomato.viewModels.MainViewModel

class MyAddressesBookActivity : AppCompatActivity(), AddressMenuClickListener {

    private lateinit var binding: ActivityMyAddressesBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var addressAdapter: ShowAllSavedAddressAdapter
    private val myHelper by lazy { MyHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        binding = ActivityMyAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#F3F4FA")

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.backButton.setOnClickListener {
            finish()
        }

        val appPreferences =  AppSharedPreferences(this)
        val isSkipBtnClick = appPreferences.getBoolean("skipBtnClick")

        // when user click on skip btn
        if (isSkipBtnClick) {
            binding.blinketCard.visibility = View.GONE
        }else{
            binding.blinketCard.visibility = View.VISIBLE
        }

        binding.addLocationCard.setOnClickListener {
            val intent = Intent(this, AddLocationFromMapActivity::class.java)
            startActivity(intent)
        }

        addressAdapter = ShowAllSavedAddressAdapter(this, "addressbookActivity")
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = addressAdapter

        showAllSavedAddresses()
    }

    private fun showAllSavedAddresses() {

        mainViewModel.getAllAddresses(myHelper.numberIs())
        mainViewModel.addresses.observe(this) { addresses ->

            if (addresses.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.dividerView3.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.dividerView3.visibility = View.VISIBLE
                addressAdapter.updateAddresses(addresses)
                addressAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
    }

    override fun menuClick(address: UserSavedAddress, action: String) {
        when (action) {
            "edit" -> {
              //  Toast.makeText(this, "${address.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, AddLocationFromMapActivity::class.java)
                intent.putExtra("selectedLocation", address.selectedLocation)
                intent.putExtra("addressId", address.id)
                startActivity(intent)
            }
            "delete" -> {
                showDeleteConfirmationDialog(address)
            }
        }
    }

    private fun showDeleteConfirmationDialog(address: UserSavedAddress) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this address?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            mainViewModel.deleteAddress(address.id)
            showAllSavedAddresses()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    override fun addressClick(userSavedAddress: UserSavedAddress) {
    }
}
