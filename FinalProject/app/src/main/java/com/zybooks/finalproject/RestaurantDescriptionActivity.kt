package com.zybooks.finalproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class RestaurantDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        val selectedRestaurantJson = intent.getStringExtra("selectedRestaurant")
        val selectedRestaurant = Gson().fromJson(selectedRestaurantJson, MainActivity.Business::class.java)

        val restaurantNameTextView = findViewById<TextView>(R.id.restaurantNameTextView)
        val addressTextView = findViewById<TextView>(R.id.addressTextView)
        val imageView1 = findViewById<ImageView>(R.id.imageView1)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)
        val imageView3 = findViewById<ImageView>(R.id.imageView3)

        restaurantNameTextView.text = selectedRestaurant.name
        addressTextView.text = selectedRestaurant.address


    }
}
