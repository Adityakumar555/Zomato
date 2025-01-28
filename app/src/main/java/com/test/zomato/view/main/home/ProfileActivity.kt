package com.test.zomato.view.main.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.test.zomato.databinding.ActivityProfileBinding
import com.test.zomato.view.login.UserSignUpActivity
import com.test.zomato.view.main.placeOrders.YourOrderActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.joinGold.setOnClickListener {
            startActivity(Intent(this, JoinGoldActivity::class.java))
        }

        binding.foodOrderSection.setOnClickListener {
            Toast.makeText(this, "Food Order Section clicked", Toast.LENGTH_SHORT).show()
        }

        binding.foodOrderLayout.setOnClickListener {
            Toast.makeText(this, "Food Order Layout clicked", Toast.LENGTH_SHORT).show()
        }

        binding.yourOrder.setOnClickListener {
            val intent = Intent(this, YourOrderActivity::class.java)
            startActivity(intent)
        }

        binding.yourProfile.setOnClickListener {
            val intent = Intent(this, UserProfileDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.favoriteOrder.setOnClickListener {
            Toast.makeText(this, "Favorite Orders clicked", Toast.LENGTH_SHORT).show()
        }

        binding.manageRecommendations.setOnClickListener {
            Toast.makeText(this, "Manage Recommendations clicked", Toast.LENGTH_SHORT).show()
        }

        binding.orderOnTrain.setOnClickListener {
            Toast.makeText(this, "Order on Train clicked", Toast.LENGTH_SHORT).show()
        }

        binding.addressBook.setOnClickListener {
            Toast.makeText(this, "Address Book clicked", Toast.LENGTH_SHORT).show()
        }

        binding.hiddenRestaurants.setOnClickListener {
            Toast.makeText(this, "Hidden Restaurants clicked", Toast.LENGTH_SHORT).show()
        }

        binding.onlineOrderingHelp.setOnClickListener {
            Toast.makeText(this, "Online Ordering Help clicked", Toast.LENGTH_SHORT).show()
        }

        binding.yourDiningTransctions.setOnClickListener {
            Toast.makeText(this, "Your Dining Transactions clicked", Toast.LENGTH_SHORT).show()
        }

        binding.yourDiningRewards.setOnClickListener {
            Toast.makeText(this, "Your Dining Rewards clicked", Toast.LENGTH_SHORT).show()
        }

        binding.yourBookings.setOnClickListener {
            Toast.makeText(this, "Your Bookings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.diningHelp.setOnClickListener {
            Toast.makeText(this, "Dining Help clicked", Toast.LENGTH_SHORT).show()
        }

        binding.diningSettings.setOnClickListener {
            Toast.makeText(this, "Dining Settings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.frequentlyAskedQuestions.setOnClickListener {
            Toast.makeText(this, "Frequently Asked Questions clicked", Toast.LENGTH_SHORT).show()
        }

        binding.about.setOnClickListener {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show()
        }

        binding.sendFeedback.setOnClickListener {
            Toast.makeText(this, "Send Feedback clicked", Toast.LENGTH_SHORT).show()
        }

        binding.reportSafetyEmergency.setOnClickListener {
            Toast.makeText(this, "Report a Safety Emergency clicked", Toast.LENGTH_SHORT).show()
        }

        binding.settings.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.logout.setOnClickListener {

            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()

            getSharedPreferences("AppPreferences", MODE_PRIVATE).edit().clear().apply()

            val intent = Intent(this,UserSignUpActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.yourImpact.setOnClickListener {
            Toast.makeText(this, "Your Impact clicked", Toast.LENGTH_SHORT).show()
        }

        binding.getFeedingIndiaReceipt.setOnClickListener {
            Toast.makeText(this, "Get Feeding India Receipt clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
