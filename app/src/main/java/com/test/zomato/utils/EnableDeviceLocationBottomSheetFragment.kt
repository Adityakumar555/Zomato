package com.test.zomato.utils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.zomato.databinding.FragmentEnableDeviceLocationBottomSheetBinding
import com.test.zomato.view.main.MainActivity

class EnableDeviceLocationBottomSheetFragment : BottomSheetDialogFragment() {

    // Declare the ViewBinding property using lateinit
    private lateinit var binding: FragmentEnableDeviceLocationBottomSheetBinding
    private val myHelper: MyHelper by lazy { MyHelper(requireActivity()) }
    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        binding = FragmentEnableDeviceLocationBottomSheetBinding.inflate(inflater, container, false)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                } else {
                }
            }

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.enableLocation.setOnClickListener {
            if (myHelper.checkPermission()) {
                if (!myHelper.isLocationEnable()) {
                    myHelper.onGPS(resultLauncher)
                }
            } else {
                myHelper.requestLocationPermission(requireActivity())
            }
            dismiss()
        }

        return binding.root
    }



}
