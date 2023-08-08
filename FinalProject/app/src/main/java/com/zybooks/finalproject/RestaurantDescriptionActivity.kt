package com.zybooks.finalproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestaurantDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        val selectedRestaurant = intent.getSerializableExtra("selectedRestaurant") as MainActivity.Business

        val restaurantNameTextView = findViewById<TextView>(R.id.restaurantNameTextView)
        val addressTextView = findViewById<TextView>(R.id.addressTextView)

        restaurantNameTextView.text = selectedRestaurant.name
        // addressTextView.text = selectedRestaurant.location.address
        // ... Set other views
    }
}
