package com.test.zomato.view.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.test.zomato.R
import com.test.zomato.databinding.FragmentLiveBinding
import com.test.zomato.utils.AppSharedPreferences
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.login.repository.UserViewModel

class LiveFragment : Fragment() {

    private lateinit var binding: FragmentLiveBinding
    private val myHelper by lazy { MyHelper(requireActivity()) }
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        binding = FragmentLiveBinding.inflate(inflater, container, false)

        myHelper.setStatusBarIconColor(requireActivity(),true)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        val appPreferences = activity?.let { AppSharedPreferences(it) }
        val isSkipBtnClick = appPreferences?.getBoolean("skipBtnClick")

        if (isSkipBtnClick == true) {
            binding.profile.visibility = View.GONE
            binding.menuIcon.visibility = View.VISIBLE

            binding.menuIcon.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                activity?.startActivity(intent)
            }

        }else{
            binding.profile.visibility = View.VISIBLE
            binding.menuIcon.visibility = View.GONE
        }


        val videoUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.zomato_event)
        binding.videoView.setVideoURI(videoUri)

        binding.videoView.start()

        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.setVolume(0f, 0f)
        }

        binding.videoView.setOnCompletionListener {
            binding.videoView.start()
        }

        binding.profile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        fetchUserData(myHelper.numberIs())


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        fetchUserData(myHelper.numberIs())
    }


    private fun fetchUserData(userPhoneNumber: String) {
        if (userPhoneNumber.isNotEmpty()) {
            userViewModel.getUserByPhoneNumber(userPhoneNumber)

            activity?.let {
                userViewModel.userLiveData.observe(it) { user ->
                    user?.let {
                        Log.d("userDataProfile", "fetchUserData: $user")

                        if (!user.imageUrl.isNullOrEmpty()) {
                            Glide.with(this).load(user.imageUrl).into(binding.userImage)
                            binding.userFirstCharacter.visibility = View.GONE
                            binding.userImage.visibility = View.VISIBLE
                        } else {
                            binding.userFirstCharacter.visibility = View.VISIBLE
                            binding.userImage.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

}
