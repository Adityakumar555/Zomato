package com.test.zomato.view.location

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.test.zomato.R
import com.test.zomato.databinding.FragmentEnterCompleteAddressBottomSheetBinding
import com.test.zomato.viewModels.MainViewModel

class EnterCompleteAddressBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEnterCompleteAddressBottomSheetBinding
    private lateinit var mainViewModel: MainViewModel
    private var selectedAddressType: String = "Home" // Default to "Home"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterCompleteAddressBottomSheetBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val selectedAddress = arguments?.getString("address")

        // Set the selected address in the TextView
        binding.selectedAddress.text = selectedAddress ?: "Address not available"

        binding.selectedAddressLayout.setOnClickListener {
            dismiss()
        }


        binding.EnterCompleteAddressLayout.visibility = View.VISIBLE
        binding.bottomBtnCard.visibility = View.VISIBLE
        binding.confirmYourAddressLayout.visibility = View.GONE
        binding.bottomBtnCard2.visibility = View.GONE

        // Handle saving user details and address
        binding.saveAddressInDb.setOnClickListener {
            val receiverName = binding.receiverName.text.toString()
            val receiverNumber = binding.receiverNumber.text.toString()
            val selectedLocation = binding.selectedAddress.text.toString()
            val houseAddress = binding.flatHouseNoFloorBuilding.text.toString()
            val nearbyLandmark = binding.emailInput.text.toString()

            // Save the address using the ViewModel
            mainViewModel.saveAddress(
                receiverName, receiverNumber, selectedAddressType,
                selectedLocation, houseAddress, nearbyLandmark
            )

        }

        binding.changeAddress.setOnClickListener {
            binding.EnterCompleteAddressLayout.visibility = View.VISIBLE
            binding.bottomBtnCard.visibility = View.VISIBLE
            binding.confirmYourAddressLayout.visibility = View.GONE
            binding.bottomBtnCard2.visibility = View.GONE
        }

        binding.addAddressInDb.setOnClickListener {
            binding.EnterCompleteAddressLayout.visibility = View.GONE
            binding.bottomBtnCard.visibility = View.GONE
            binding.bottomBtnCard2.visibility = View.VISIBLE
            binding.confirmYourAddressLayout.visibility = View.VISIBLE

            val receiverName = binding.receiverName.text.toString()
            val receiverNumber = binding.receiverNumber.text.toString()
            val selectedLocation = binding.selectedAddress.text.toString()
            val houseAddress = binding.flatHouseNoFloorBuilding.text.toString()
            val nearbyLandmark = binding.emailInput.text.toString()

            binding.confirmLocationText.text = "Confirm your Address"
            binding.deliveryAt.text = "Delivery at $selectedAddressType"
            binding.address.text = "$houseAddress, $selectedLocation, $nearbyLandmark"
            binding.confirmUserName.text = "${receiverName},"
            binding.confirmUserNumber.text = receiverNumber
        }


        // Set click listeners for each card
        setCardClickListeners()

        // Set the default selected card (Home card)
        onCardClick(
            card = binding.home,
            iconId = R.id.home_icon,
            textId = R.id.home_text
        )

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.userDetailsCard.setOnClickListener {
            binding.userDetailsCard.visibility = View.GONE
            binding.receiverDetails.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun setCardClickListeners() {
        binding.home.setOnClickListener {
            selectedAddressType = "Home"
            onCardClick(
                card = binding.home,
                iconId = R.id.home_icon,
                textId = R.id.home_text
            )
        }

        binding.work.setOnClickListener {
            selectedAddressType = "Work"
            onCardClick(
                card = binding.work,
                iconId = R.id.work_icon,
                textId = R.id.work_text
            )
        }

        binding.hotel.setOnClickListener {
            selectedAddressType = "Hotel"
            onCardClick(
                card = binding.hotel,
                iconId = R.id.hotel_icon,
                textId = R.id.hotel_text
            )
        }

        binding.other.setOnClickListener {
            selectedAddressType = "Other"
            onCardClick(
                card = binding.other,
                iconId = R.id.other_icon,
                textId = R.id.other_text
            )
        }
    }

/*    private fun setBottomSheetSize() {
        // Fix the Bottom Sheet size (you can adjust these values as per your requirements)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val layoutParams = it.layoutParams
            layoutParams.height = resources.getDimensionPixelSize("400dp")  // Set fixed height
            layoutParams.width = resources.getDimensionPixelSize(m)   // Set fixed width
            it.layoutParams = layoutParams
        }
    }*/

  /*  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            (this as? BottomSheetDialog)
                ?.behavior
                ?.setPeekHeight(BottomSheetBehavior.STATE_COLLAPSED, true)
        }
    }*/

    private fun onCardClick(card: MaterialCardView, iconId: Int, textId: Int) {
        // First, reset all the cards to default state
        resetAllCards()

        // Now, update the clicked card's appearance
        card.setCardBackgroundColor(Color.parseColor("#FFF5F6"))  // Light pink color
        card.strokeColor = resources.getColor(R.color.colorPrimary)

        // Change the icon and text color to the primary color
        val icon = card.findViewById<ImageView>(iconId)
        val text = card.findViewById<TextView>(textId)

        icon?.setColorFilter(resources.getColor(R.color.colorPrimary))  // Set icon color to primary color
        text?.setTextColor(resources.getColor(R.color.colorPrimary))    // Set text color to primary color
    }

    private fun resetAllCards() {
        // Reset all cards to their default state
        resetCard(binding.home, R.id.home_icon, R.id.home_text)
        resetCard(binding.work, R.id.work_icon, R.id.work_text)
        resetCard(binding.hotel, R.id.hotel_icon, R.id.hotel_text)
        resetCard(binding.other, R.id.other_icon, R.id.other_text)
    }

    private fun resetCard(card: MaterialCardView, iconId: Int, textId: Int) {
        // Reset the background color, icon color, and text color of the card
        card.setCardBackgroundColor(Color.WHITE)  // Reset background to white
        card.strokeColor = Color.GRAY

        val icon = card.findViewById<ImageView>(iconId)
        val text = card.findViewById<TextView>(textId)

        icon?.setColorFilter(Color.BLACK)  // Reset icon color to black
        text?.setTextColor(Color.BLACK)    // Reset text color to black
    }
}
