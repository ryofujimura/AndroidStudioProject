package com.zybooks.restaurantsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class RestaurantDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        val restaurantName = intent.getStringExtra("selectedRestaurantName")
        val restaurantImageUrl = intent.getStringExtra("selectedRestaurantImageUrl")
        // Retrieve additional image URLs if needed

        val restaurantNameTextView = findViewById<TextView>(R.id.restaurantNameTextView)
        val restaurantImageView = findViewById<ImageView>(R.id.restaurantImageView1)
        // Add additional image views here

        restaurantNameTextView.text = restaurantName
        Glide.with(this).load(restaurantImageUrl).into(restaurantImageView)
        // Load additional images using Glide here
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
