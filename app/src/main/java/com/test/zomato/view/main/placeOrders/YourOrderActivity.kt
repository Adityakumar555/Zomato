package com.test.zomato.view.main.placeOrders

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.zomato.databinding.ActivityYourOrderBinding
import com.test.zomato.view.main.home.adapter.OrderAdapter
import com.test.zomato.repository.roomDb.RoomDbViewModel

class YourOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourOrderBinding
    private lateinit var roomDbViewModel: RoomDbViewModel
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYourOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomDbViewModel = ViewModelProvider(this)[RoomDbViewModel::class.java]

        roomDbViewModel.fetchOrdersFromDb()

        orderAdapter = OrderAdapter()

        // Observe the orders in the database
        roomDbViewModel.fetchOrdersInDb.observe(this) { ordersWithFoodItems ->
            // Update the list in the adapter
            orderAdapter.updateList(ordersWithFoodItems)
            orderAdapter.notifyDataSetChanged()

            // Check if the orders list is empty
            if (ordersWithFoodItems.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyOrderList.visibility = View.VISIBLE
                binding.searchLayout.visibility = View.GONE // Hide search box if no orders
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyOrderList.visibility = View.GONE
                binding.searchLayout.visibility = View.VISIBLE
            }
        }

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = orderAdapter

        // Handle back button click
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
