package com.zybooks.finalproject

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class RestaurantDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        val selectedRestaurantPosition = intent.getIntExtra("selectedRestaurantPosition", -1)

        if (selectedRestaurantPosition != -1) {
            val selectedRestaurant = MainActivity.restaurants.businesses[selectedRestaurantPosition]

            val restaurantImageView1 = findViewById<ImageView>(R.id.restaurantImageView1)
            val restaurantImageView2 = findViewById<ImageView>(R.id.restaurantImageView2)
            val restaurantImageView3 = findViewById<ImageView>(R.id.restaurantImageView3)

            val imageUrls = selectedRestaurant.image_url // Assuming 'photos' is a list of image URLs

            if (imageUrls.isNotEmpty()) {
                Glide.with(this).load(imageUrls[0]).into(restaurantImageView1)
                Glide.with(this).load(imageUrls[1]).into(restaurantImageView2)
                Glide.with(this).load(imageUrls[2]).into(restaurantImageView3)
            }
        }
    }
}
