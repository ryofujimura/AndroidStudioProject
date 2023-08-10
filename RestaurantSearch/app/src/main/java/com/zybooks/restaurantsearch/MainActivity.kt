package com.zybooks.restaurantsearch

// 110fzSdHM6bFShApZ7HVpEFw6hJwfNBiXlfp4lSL4u6GU64MJOJxLUOFkHx2VnezuCHHNtnTFbQPkuDjMkHabmHCV4tM2NV2UZRssuuFcJlqVXn9107TajsE8pu7YXYx

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import com.google.gson.Gson
import java.io.IOException
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var restaurantListView: ListView
    private lateinit var loadingProgressBar: ProgressBar

    companion object {
        lateinit var restaurants: YelpApiResponse
    }

    private val yelpApiKey = "110fzSdHM6bFShApZ7HVpEFw6hJwfNBiXlfp4lSL4u6GU64MJOJxLUOFkHx2VnezuCHHNtnTFbQPkuDjMkHabmHCV4tM2NV2UZRssuuFcJlqVXn9107TajsE8pu7YXYx"
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationEditText = findViewById(R.id.locationEditText)
        searchButton = findViewById(R.id.searchButton)
        restaurantListView = findViewById(R.id.restaurantListView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        searchButton.setOnClickListener {
            val location = locationEditText.text.toString()
            loadingProgressBar.visibility = View.VISIBLE
            searchRestaurants(location)

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        // Inside your onItemClick listener
        restaurantListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, RestaurantDescriptionActivity::class.java)
            val selectedRestaurant = restaurants.businesses[position]

            intent.putExtra("selectedRestaurantName", selectedRestaurant.name)
            intent.putExtra("selectedRestaurantImageUrl", selectedRestaurant.image_url)
            // Add additional image URLs here if needed

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun searchRestaurants(location: String) {
        val url = "https://api.yelp.com/v3/businesses/search?term=restaurant&location=$location"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $yelpApiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                restaurants = gson.fromJson(responseBody, YelpApiResponse::class.java)

                runOnUiThread {
                    val adapter = object : ArrayAdapter<Business>(
                        this@MainActivity,
                        R.layout.list_item_restaurant,
                        restaurants.businesses
                    ) {
                        @SuppressLint("ViewHolder")
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val itemView =
                                layoutInflater.inflate(
                                    R.layout.list_item_restaurant,
                                    parent,
                                    false
                                )
                            val restaurant = getItem(position)
                            val restaurantImageView =
                                itemView.findViewById<ImageView>(R.id.restaurantImageView)
                            val restaurantNameTextView = itemView.findViewById<TextView>(R.id.restaurantNameTextView)

                            if (restaurant != null) {
                                restaurantNameTextView.text = restaurant.name
                            }
                            if (restaurant != null) {
                                Glide.with(this@MainActivity).load(restaurant.image_url).into(
                                    restaurantImageView
                                )
                            }

                            return itemView
                        }
                    }
                    restaurantListView.adapter = adapter
                    loadingProgressBar.visibility = View.GONE

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    data class YelpApiResponse(val businesses: List<Business>)
    data class Business(val name: String,val image_url: String)
}
