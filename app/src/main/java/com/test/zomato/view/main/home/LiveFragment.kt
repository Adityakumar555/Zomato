package com.test.zomato.view.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.zomato.R
import com.test.zomato.databinding.FragmentLiveBinding
import com.test.zomato.utils.MyHelper

class LiveFragment : Fragment() {

    private lateinit var binding: FragmentLiveBinding
    private val myHelper by lazy { MyHelper(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        binding = FragmentLiveBinding.inflate(inflater, container, false)

        myHelper.setStatusBarIconColor(requireActivity(),true)

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

        return binding.root
    }
}
