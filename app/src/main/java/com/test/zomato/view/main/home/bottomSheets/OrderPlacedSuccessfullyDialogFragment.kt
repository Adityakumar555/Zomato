package com.test.zomato.view.main.home.bottomSheets

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.zomato.R
import com.test.zomato.databinding.FragmentOrderPlacedSuccessfullyDialogBinding
import com.test.zomato.view.main.MainActivity
import com.test.zomato.view.orders.interfaces.OrderPlcaeClickListener

class OrderPlacedSuccessfullyDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentOrderPlacedSuccessfullyDialogBinding
    private var listner: OrderPlcaeClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listner = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderPlacedSuccessfullyDialogBinding.inflate(inflater, container, false)

        // Glide.with(SplashScreenActivity.this).load(R.drawable.note_splash_screen).into(binding.splashIcon);

        // show note gif in splash onboarding
        Glide.with(this)
            .asGif()
            .load(R.drawable.order_placed_successfull) // .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .listener(object : RequestListener<GifDrawable?> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.setLoopCount(1)
                    return false
                }
            }).into(binding.imageView)


        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        }, 7000)


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            // Set the width and height of the dialog (optional)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
