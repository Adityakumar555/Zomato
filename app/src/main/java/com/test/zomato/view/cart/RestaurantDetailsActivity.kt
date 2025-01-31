package com.test.zomato.view.cart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.zomato.R
import com.test.zomato.databinding.ActivityRestaurantDetailsBinding
import com.test.zomato.cartDB.CartAndOrderViewModel
import com.test.zomato.view.cart.adapter.ShowFoodInRestaurantDetailsAdapter
import com.test.zomato.view.main.home.bottomSheets.AddFoodInCartBottomSheet
import com.test.zomato.view.cart.interfaces.AddFoodClickListener
import com.test.zomato.view.cart.interfaces.OnBottomSheetActionListener
import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.main.home.models.RestaurantDetails
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class RestaurantDetailsActivity : AppCompatActivity(), AddFoodClickListener,
    OnBottomSheetActionListener {

    private lateinit var binding: ActivityRestaurantDetailsBinding
    private lateinit var roomDbViewModel: CartAndOrderViewModel
    private lateinit var showFoodInRestaurantDetailsAdapter: ShowFoodInRestaurantDetailsAdapter
    private var currentRestaurantId: Int? = null
    private var restaurantDetails: RestaurantDetails? = null
    private var totalQuantity = 0
    private lateinit var updatedFoodList: MutableList<FoodItem> // Make it mutable

    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.WHITE



        binding.backButton.setOnClickListener {
            finish()
        }

        roomDbViewModel = ViewModelProvider(this)[CartAndOrderViewModel::class.java]

        // Retrieve restaurant details from intent
        restaurantDetails = intent.getParcelableExtra("restaurantDetails")
        Log.d("RestaurantDetails", "Received restaurant details: $restaurantDetails")

        if (restaurantDetails != null) {
            restaurantDetails?.let {
                binding.restaurantName.text = it.restaurantName
                binding.toolbarRestaurantName.text = it.restaurantName
                binding.ratingText.text = it.rating.toString()
                binding.takenTime.text = it.time
                binding.distance.text = it.distance
                binding.location.text = it.restaurantLocation
                binding.discountText.text = it.offerUpToText
                binding.offers.text = "${it.totalOffers} offers"

                currentRestaurantId = it.id

                // Fetch food items for the current restaurant and update UI
                fetchAndMergeFoodItems(it)
            }

            // Setup the expand/collapse functionality for food list
            expendAndCollapse()
        }
    }

    private fun fetchAndMergeFoodItems(restaurantDetails: RestaurantDetails) {
        roomDbViewModel.fetchAndUpdateCartItems()

        roomDbViewModel.fetchFoodItemsInCart.observe(this, Observer { foodItems ->
            val currentRestaurantFoodItems = foodItems.filter { it.restaurantId == currentRestaurantId }

            // Create a mutable list of updated food items
            updatedFoodList = restaurantDetails.recommendedFoodList.map { recommendedFood ->
                val cartFoodItem = currentRestaurantFoodItems.find { it.id == recommendedFood.id }
                if (cartFoodItem != null) {
                    recommendedFood.foodQuantity = cartFoodItem.foodQuantity
                }
                recommendedFood
            }.toMutableList()

            // Update total quantity
            totalQuantity = currentRestaurantFoodItems.sumOf { it.foodQuantity }
            updateCartItemCount(totalQuantity)

            if (updatedFoodList.isNotEmpty()) {
                showFoodInRestaurantDetailsAdapter = ShowFoodInRestaurantDetailsAdapter(updatedFoodList, this)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = showFoodInRestaurantDetailsAdapter
            } else {
                Toast.makeText(this, "No recommended food items available", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCartItemCount(totalQuantity: Int) {
        Log.d("RestaurantDetailsQuantity", "Total items in cart for restaurant: ${totalQuantity}")

        // Show or hide the order item count layout
        if (totalQuantity > 0) {
            binding.orderItemCountLayout.visibility = View.VISIBLE
            binding.itemAddedCount.text =
                "${totalQuantity.toString()} item added"

            binding.orderItemCountLayout.setOnClickListener {
                val intent = Intent(this, ShowCartFoodDetailsActivity::class.java)
                intent.putExtra("restaurantDetails", restaurantDetails)
                startActivity(intent)
            }


           val party = Party(
                speed = 0f,
                maxSpeed = 20f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                position = Position.Relative(0.5, 0.3),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            )

            Handler(Looper.getMainLooper()).postDelayed({
                binding.konfettiView.start(party)
                binding.konfettiView.setBackgroundColor(Color.parseColor("#3F51B5"))
            },500)


            Handler(Looper.getMainLooper()).postDelayed({
                binding.konfettiView.setBackgroundColor(Color.parseColor("#FF3044"))
            },2500)



        } else {
            binding.orderItemCountLayout.visibility = View.GONE
        }
    }

    private fun expendAndCollapse() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.showFood.setImageResource(R.drawable.arrow_drop_up_icon)

        binding.showFood.setOnClickListener {
            if (isOpen) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.showFood.setImageResource(R.drawable.arrow_drop_up_icon)
                isOpen = false
            } else {
                binding.showFood.setImageResource(R.drawable.arrow_drop_down_icon)
                binding.recyclerView.visibility = View.GONE
                isOpen = true
            }
        }
    }

    override fun onFoodClick(foodItem: FoodItem) {
        val bottomSheet = AddFoodInCartBottomSheet()
        val bundle = Bundle()
        bundle.putParcelable("foodItem", foodItem)
        bottomSheet.arguments = bundle
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    override fun onFoodQuantityChange(foodItem: FoodItem) {
        if (foodItem.foodQuantity == 0) {
            Toast.makeText(this, "${foodItem.foodName} has been removed from the cart", Toast.LENGTH_SHORT).show()
            roomDbViewModel.deleteFoodItemById(foodItem.id)
        } else {
            roomDbViewModel.changeFoodQuantity(foodItem.foodQuantity, foodItem.id)
        }
    }

    override fun onFoodItemAdded() {
        roomDbViewModel.fetchAndUpdateCartItems()
        showFoodInRestaurantDetailsAdapter.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        roomDbViewModel.fetchAndUpdateCartItems()
    }

    override fun onBottomSheetDismissed() {
        roomDbViewModel.fetchAndUpdateCartItems()
        showFoodInRestaurantDetailsAdapter.notifyDataSetChanged()
    }
}

