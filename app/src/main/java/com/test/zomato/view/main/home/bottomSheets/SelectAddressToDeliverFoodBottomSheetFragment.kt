package com.test.zomato.view.main.home.bottomSheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.zomato.databinding.FragmentSelectAddressToDeliverFoodBottomSheetBinding
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.cart.ShowCartFoodDetailsActivity
import com.test.zomato.view.main.home.interfaces.SelectAddressClickListener

class SelectAddressToDeliverFoodBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSelectAddressToDeliverFoodBottomSheetBinding
    private lateinit var myHelper:MyHelper

    private var listner :SelectAddressClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listner = context as ShowCartFoodDetailsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectAddressToDeliverFoodBottomSheetBinding.inflate(inflater, container, false)

        myHelper = MyHelper(requireActivity())

        val address = myHelper.extractAddressDetails(myHelper.getLatitude(),myHelper.getLongitude())

        binding.location.text = address?.fullAddress

        binding.addressCard.setOnClickListener {
            listner?.addressSelectedNowPlaceTheOrder()
            dismiss()
        }


        return binding.root
    }


}
