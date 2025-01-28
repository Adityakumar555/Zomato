package com.test.zomato.view.main.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.zomato.R
import com.test.zomato.databinding.FragmentDiningBinding
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.location.SelectAddressActivity
import com.test.zomato.view.main.home.adapter.BarAdapter
import com.test.zomato.view.main.home.models.BarData
import com.test.zomato.viewModels.MainViewModel

class DiningFragment : Fragment() {

    private lateinit var binding: FragmentDiningBinding
    private val myHelper by lazy { MyHelper(requireActivity()) }
    private lateinit var barAdapter: BarAdapter

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiningBinding.inflate(inflater, container, false)


        myHelper.setStatusBarIconColor(requireActivity(), false)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


        val dummyBarList = listOf(
            BarData(
                restaurantName = "Noida Social",
                rating = 4.5f,
                address = "DLF Mall of India, Sector 18, Noida",
                distance = "4.5 km",
                cuisines1 = "North Indian",
                cuisines2 = "Chinese",
                costForTwo = "₹1500 for two",
                bankBenefits = "Bank benefits + 1 more",
                sliderImages = listOf(
                    "https://b.zmtcdn.com/data/pictures/6/18767086/5703dac63e1d4deab23e40fb932091c9.jpg?fit=around|960:500&crop=960:500;*,*",
                    "https://content.jdmagicbox.com/comp/noida/x3/011pxx11.xx11.211221112804.i3x3/catalogue/toy-boy-noida-sector-38-noida-pubs-xx2q1226ph.jpg",
                    "https://d24l7ypac8dw56.cloudfront.net/MenuImages/IMG20230211WA0008-90966-102675.jpg",
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/tr:e-sharpen,l-text,ie-U2xvc2hvdXQuY29t,lfo-center,co-FFFFFF50,fs-20,tg-b,l-end/ambience-of-clinque-264.jpg"
                )
            ),
            BarData(
                restaurantName = "The Barbeque Nation",
                rating = 4.2f,
                address = "Sector 32, Noida",
                distance = "3.5 km",
                cuisines1 = "Barbeque",
                cuisines2 = "Desserts",
                costForTwo = "₹1800 for two",
                bankBenefits = "Special Offers",
                sliderImages = listOf(
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/tr:e-sharpen,l-text,ie-U2xvc2hvdXQuY29t,lfo-center,co-FFFFFF50,fs-20,tg-b,l-end/interior-decor-of-clinque-144.jpg",
                    "https://content.jdmagicbox.com/comp/noida/i1/011pxx11.xx11.220623163025.h6i1/catalogue/clinque-noida-sector-38-noida-lounge-bars-jv9vdo0tav.jpg",
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/seating-area-of-the-bar-company-885.JPG",
                    "https://b.zmtcdn.com/data/pictures/6/18767086/5a9f5a35a9c727e341bedc1b3ea753f5.jpeg"
                )
            ),
            BarData(
                restaurantName = "The Whiskey Bar",
                rating = 4.8f,
                address = "Connaught Place, New Delhi",
                distance = "2.1 km",
                cuisines1 = "Continental",
                cuisines2 = "Drinks",
                costForTwo = "₹2500 for two",
                bankBenefits = "10% OFF with Credit Card",
                sliderImages = listOf(
                    "https://content.jdmagicbox.com/comp/delhi/81/011pxx11.xx11.220303094658.0qqp/catalogue/the-whiskey-bar-connaught-place-new-delhi.jpg",
                    "https://b.zmtcdn.com/data/pictures/1/18767086/5703dac63e1d4deab23e40fb932091c9.jpg?fit=around|960:500&crop=960:500;*,*",
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/seating-area-of-the-bar-company-885.JPG",
                    "https://d24l7ypac8dw56.cloudfront.net/MenuImages/IMG20230211WA0008-90966-102675.jpg"
                )
            ),
            BarData(
                restaurantName = "The Royal Lounge",
                rating = 4.3f,
                address = "Sector 47, Gurgaon",
                distance = "7.8 km",
                cuisines1 = "Italian",
                cuisines2 = "European",
                costForTwo = "₹2200 for two",
                bankBenefits = "20% OFF with Debit Card",
                sliderImages = listOf(
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/interior-decor-of-clinque-144.jpg",
                    "https://d24l7ypac8dw56.cloudfront.net/MenuImages/IMG20230211WA0008-90966-102675.jpg",
                    "https://b.zmtcdn.com/data/pictures/6/18767086/5703dac63e1d4deab23e40fb932091c9.jpg?fit=around|960:500&crop=960:500;*,*",
                    "https://content.jdmagicbox.com/comp/noida/x3/011pxx11.xx11.211221112804.i3x3/catalogue/toy-boy-noida-sector-38-noida-pubs-xx2q1226ph.jpg"
                )
            ),
            BarData(
                restaurantName = "The Beer Cafe",
                rating = 4.1f,
                address = "Saket, New Delhi",
                distance = "5.2 km",
                cuisines1 = "Beer",
                cuisines2 = "Bar Snacks",
                costForTwo = "₹1400 for two",
                bankBenefits = "Exclusive Bank Offers",
                sliderImages = listOf(
                    "https://content.jdmagicbox.com/comp/delhi/81/011pxx11.xx11.220303094658.0qqp/catalogue/the-whiskey-bar-connaught-place-new-delhi.jpg",
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/seating-area-of-the-bar-company-885.JPG",
                    "https://b.zmtcdn.com/data/pictures/6/18767086/5a9f5a35a9c727e341bedc1b3ea753f5.jpeg",
                    "https://ik.imagekit.io/pu0hxo64d/uploads/gallery/tr:e-sharpen,l-text,ie-U2xvc2hvdXQuY29t,lfo-center,co-FFFFFF50,fs-20,tg-b,l-end/ambience-of-clinque-264.jpg"
                )
            )
        )


        barAdapter = BarAdapter(dummyBarList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = barAdapter


        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {

                val searchText = p0.toString().trim()

                val list = if (searchText.isNotEmpty()) {
                    dummyBarList.filter {
                        it.restaurantName.contains(searchText, ignoreCase = true)
                                || it.address.contains(searchText, ignoreCase = true)
                    }
                } else {
                    dummyBarList
                }

                barAdapter.updateList(list)
                barAdapter.notifyDataSetChanged()
            }
        })



        binding.profile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        binding.userLocation.setOnClickListener {
            val intent = Intent(context, SelectAddressActivity::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)

        }

        setLocationOnToolbar()

        return binding.root
    }

    private fun setLocationOnToolbar() {
        //  val locationData = myHelper.extractAddressDetails(mainViewModel.latitude, mainViewModel.longitude)
        val locationData =
            myHelper.extractAddressDetails(myHelper.getLatitude(), myHelper.getLongitude())
        // Update the TextView elements with fetched data
        binding.userBlockLocation.text = locationData?.block
        binding.address.text = "${locationData?.locality},${locationData?.state}"
    }

}
