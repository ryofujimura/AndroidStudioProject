package com.zybooks.finalproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestaurantDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        val selectedRestaurantName = intent.getStringExtra("selectedRestaurant")

        val restaurantNameTextView = findViewById<TextView>(R.id.restaurantNameTextView)
        val addressTextView = findViewById<TextView>(R.id.addressTextView)

        restaurantNameTextView.text = selectedRestaurantName
        addressTextView.text = "Address goes here"
        // ... Set other views
    }
}
