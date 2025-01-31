package com.test.zomato.view.cart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.test.zomato.R
import com.test.zomato.databinding.ActivityShowOrderFoodDetailsBinding
import com.test.zomato.cartDB.CartAndOrderViewModel
import com.test.zomato.utils.MyHelper
import com.test.zomato.view.main.MainActivity
import com.test.zomato.view.cart.adapter.FoodItemInCartAdapter
import com.test.zomato.view.main.home.bottomSheets.OrderPlaceOrNotBottomSheetFragment
import com.test.zomato.view.main.home.bottomSheets.SelectAddressToDeliverFoodBottomSheetFragment
import com.test.zomato.view.cart.interfaces.AddFoodClickListener
import com.test.zomato.view.orders.interfaces.OrderPlcaeClickListener
import com.test.zomato.view.main.home.interfaces.SelectAddressClickListener
import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.orders.orderModels.FoodItemInOrder
import com.test.zomato.view.orders.orderModels.OrderDetails
import com.test.zomato.view.main.home.models.RestaurantDetails

class ShowCartFoodDetailsActivity : AppCompatActivity(), AddFoodClickListener,
    SelectAddressClickListener, OrderPlcaeClickListener {

    private lateinit var binding: ActivityShowOrderFoodDetailsBinding
    private var restaurantDetails: RestaurantDetails? = null
    private lateinit var roomDbViewModel: CartAndOrderViewModel
    private val myHelper by lazy { MyHelper(this) }
    private var isCouponApplied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityShowOrderFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Adityatag", "onCreate: ShowOrderFoodDetailsActivity")

        roomDbViewModel = ViewModelProvider(this)[CartAndOrderViewModel::class.java]

        window.statusBarColor = Color.WHITE


        Glide.with(this)
            .load(R.drawable.party_emoji)
            .into(binding.emojiIcon)


        binding.backButton.setOnClickListener {
            finish()
        }


        binding.applyCoupons.setOnClickListener {
            // Apply the coupon, reduce ₹62 from the total
            if (!isCouponApplied) {
                isCouponApplied = true
                updateTotalPriceWithCoupon(true)
                binding.applyCoupons.visibility = View.GONE
                binding.removeApplyCoupons.visibility = View.VISIBLE
            }
        }

        binding.removeApplyCoupons.setOnClickListener {
            // Remove the coupon, restore ₹62 to the total
            if (isCouponApplied) {
                isCouponApplied = false
                updateTotalPriceWithCoupon(false)
                binding.applyCoupons.visibility = View.VISIBLE
                binding.removeApplyCoupons.visibility = View.GONE
            }
        }


        // Get the restaurant details from the Intent
        restaurantDetails = intent.getParcelableExtra("restaurantDetails")

        // Set restaurant details
        restaurantDetails?.apply {
            binding.restaurantName.text = restaurantName
            binding.discountText.text =
                "You saved ₹${recommendedFoodList[0].foodOffer} on this order"

            binding.text8.text = "You saved ₹${recommendedFoodList[0].foodOffer} on delivery"

            binding.text11.text = "Auto-applied as your order is above ₹199"
            binding.deliveryTime.text = "Delivery in $timeToReach mins"
            binding.deliveryLocation.visibility = View.GONE
            binding.bottomLayout2.visibility = View.GONE
            binding.userDetailsCard.visibility = View.GONE
            binding.selectAddressForOrder.visibility = View.VISIBLE
            // Set total bill
            updateTotalPrice(recommendedFoodList)
        }

        fetchAllFoodAndUpdateAdapter()

        binding.selectAddressForOrder.setOnClickListener {
            val selectAddressToDeliverFoodBottomSheetFragment =
                SelectAddressToDeliverFoodBottomSheetFragment()
            selectAddressToDeliverFoodBottomSheetFragment.show(
                supportFragmentManager,
                "selectAddressToDeliverFoodBottomSheetFragment"
            )
        }

    }


    private fun updateTotalPriceWithCoupon(applyCoupon: Boolean) {
        val foodItems = roomDbViewModel.fetchFoodItemsInCart.value?.filter { it.restaurantId == restaurantDetails?.id }
            ?: emptyList()

        val totalPrice = foodItems.sumOf { it.foodPrice.toDouble() * it.foodQuantity }
        val totalSaved = foodItems.sumOf { it.foodOffer.toDouble() }

        // If the coupon is applied, reduce ₹62 from the total price
        val finalPrice = if (applyCoupon) {
            totalPrice - 62.0 // Apply coupon
        } else {
            totalPrice // No coupon applied
        }

        val remainingPrice = finalPrice - totalSaved

        binding.totalFoodPriceInButton.text = finalPrice.toInt().toString()
        binding.totalBill.text = "Total bill ₹${finalPrice.toInt()} - ₹${remainingPrice.toInt()}"
        binding.youSavedText.text = "You saved ₹${totalSaved}"
    }



    private fun fetchAllFoodAndUpdateAdapter() {
        // Observe changes in food items in cart
        roomDbViewModel.fetchAndUpdateCartItems()

        roomDbViewModel.fetchFoodItemsInCart.observe(this, Observer { foodItems ->
            val currentRestaurantFoodItems =
                foodItems.filter { it.restaurantId == restaurantDetails?.id }

            val foodList = currentRestaurantFoodItems.filter { it.foodQuantity > 0 }

            val totalQuantity = currentRestaurantFoodItems.sumOf { it.foodQuantity }

            Log.d(
                "FoodOrderDetailsQuantity",
                "Total items in cart for restaurant: ${totalQuantity}"
            )


            // If cart is empty, finish the activity
            if (totalQuantity == 0) {
                finish()
                // roomDbViewModel.fetchAndUpdateCartItems()
            }

            if (foodList.isNotEmpty()) {
                // Create the adapter for cart items
                val foodItemAdapter = FoodItemInCartAdapter(foodList, this)
                binding.cartFoodRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.cartFoodRecyclerView.adapter = foodItemAdapter

                // When the food items are updated (quantity changes), recalculate the total bill
                updateTotalPrice(currentRestaurantFoodItems)
            } else {
                Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Method to update total price
    private fun updateTotalPrice(foodItems: List<FoodItem>) {
        val totalPrice = foodItems.sumOf { it.foodPrice.toDouble() * it.foodQuantity }
        val totalSaved = foodItems.sumOf { it.foodOffer.toDouble() }
        val remainingPrice = totalPrice - totalSaved

        binding.totalFoodPriceInButton.text = totalPrice.toInt().toString()

        // Directly updating the TextViews with simple string formatting
        binding.totalBill.text = "Total bill ₹${totalPrice.toInt()} - ₹${remainingPrice.toInt()}"
        binding.youSavedText.text = "You saved ₹${totalSaved}"
    }

    override fun onFoodClick(foodItem: FoodItem) {
        // Handle food item click (if needed)
    }

    override fun onFoodQuantityChange(foodItem: FoodItem) {
        // Check if the quantity of the food item is 0
        if (foodItem.foodQuantity == 0) {
            roomDbViewModel.changeFoodQuantity(foodItem.foodQuantity, foodItem.id)
            //  roomDbViewModel.deleteFoodItemById(foodItem.id)
            roomDbViewModel.fetchAndUpdateCartItems()
        } else {
            // Update food quantity in the database
            roomDbViewModel.changeFoodQuantity(foodItem.foodQuantity, foodItem.id)
            roomDbViewModel.fetchAndUpdateCartItems()
        }

    }


    override fun addressSelectedNowPlaceTheOrder() {

        val address =
            myHelper.extractAddressDetails(myHelper.getLatitude(), myHelper.getLongitude())
        binding.location.text = address?.fullAddress

        binding.deliveryLocation.visibility = View.VISIBLE


        binding.userDetailsCard.visibility = View.VISIBLE
        binding.bottomLayout2.visibility = View.VISIBLE
        binding.selectAddressForOrder.visibility = View.GONE

        binding.placeOrder.setOnClickListener {
            // Fetch food items from the cart that have quantity > 0
            if (address == null) {
                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Fetch food items from the cart that have quantity > 0
            val currentRestaurantFoodItems =
                roomDbViewModel.fetchFoodItemsInCart.value?.filter { it.restaurantId == restaurantDetails?.id }
                    ?: emptyList()

            val foodItems = currentRestaurantFoodItems.filter { it.foodQuantity > 0 }
            if (foodItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var totalPrice = foodItems.sumOf { it.foodPrice.toDouble() * it.foodQuantity }

            if (isCouponApplied) {
                totalPrice -= 62.0
            }
            val orderPlaceOrNotBottomSheetFragment = OrderPlaceOrNotBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("location", address.fullAddress)
                    putDouble("totalPrice", totalPrice)
                }
            }
            orderPlaceOrNotBottomSheetFragment.show(
                supportFragmentManager,
                "orderPlaceBottomSheetFragment"
            )

        }
    }


    override fun orderPlaceClick() {
        placeOrder()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("showOrderDialog", true)
        startActivity(intent)
        finishAffinity()
    }


    override fun orderPlacedDialogClick() {

    }

    private fun placeOrder() {
        val address =
            myHelper.extractAddressDetails(myHelper.getLatitude(), myHelper.getLongitude())
        binding.location.text = address?.fullAddress

        val currentRestaurantFoodItems =
            roomDbViewModel.fetchFoodItemsInCart.value?.filter { it.restaurantId == restaurantDetails?.id }
                ?: emptyList()

        val foodItems = currentRestaurantFoodItems.filter { it.foodQuantity > 0 }
        if (foodItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            return
        }

        var totalPrice = foodItems.sumOf { it.foodPrice.toDouble() * it.foodQuantity }
        val totalSaved = foodItems.sumOf { it.foodOffer.toDouble() }

        if (isCouponApplied) {
            totalPrice -= 62.0
        }

        val orderDetails = OrderDetails(
            restaurantId = restaurantDetails?.id ?: 0,
            restaurantName = restaurantDetails?.restaurantName ?: "Unknown",
            totalPrice = totalPrice,
            totalSaved = totalSaved,
            deliveryAddress = address?.fullAddress ?: "Unknown",
            orderStatus = "Pending",
            timestamp = System.currentTimeMillis()
        )

        val foodItemsInOrder = foodItems.map { foodItem ->
            FoodItemInOrder(
                orderId = 0,
                foodId = foodItem.id,
                foodName = foodItem.foodName,
                foodPrice = foodItem.foodPrice,
                foodQuantity = foodItem.foodQuantity,
                foodOffer = foodItem.foodOffer,
                foodType = foodItem.foodType,
                foodSize = foodItem.foodSize,
                foodImage = foodItem.foodImage,
                restaurantId = foodItem.restaurantId
            )
        }

        roomDbViewModel.insertOrderDetails(orderDetails, foodItemsInOrder)

        roomDbViewModel.deleteFoodItemsByRestaurantId(orderDetails.restaurantId)

    }


}