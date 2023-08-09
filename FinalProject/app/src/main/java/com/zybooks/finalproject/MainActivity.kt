package com.zybooks.finalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var restaurantListView: ListView

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

        searchButton.setOnClickListener {
            val location = locationEditText.text.toString()
            searchRestaurants(location)

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        restaurantListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, RestaurantDescriptionActivity::class.java)
            val selectedRestaurant = restaurants.businesses[position]

            intent.putExtra("selectedRestaurantName", selectedRestaurant.name)
            intent.putExtra("selectedRestaurantImageUrl", selectedRestaurant.image_url)
            intent.putStringArrayListExtra("additionalImageUrls", ArrayList(selectedRestaurant.additional_image_urls))

            startActivity(intent)
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

                            val restaurantNameTextView =
                                itemView.findViewById<TextView>(R.id.restaurantNameTextView)
                            val restaurantImageView =
                                itemView.findViewById<ImageView>(R.id.restaurantImageView)

                            if (restaurant != null) {
                                restaurantNameTextView.text = restaurant.name
                            }
                            if (restaurant != null) {
                                Glide.with(this@MainActivity).load(restaurant.image_url)
                                    .into(restaurantImageView)
                            }

                            return itemView
                        }
                    }
                    restaurantListView.adapter = adapter
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    data class YelpApiResponse(val businesses: List<Business>)
    data class Business(
        val name: String,
        val image_url: String,
        val additional_image_urls: List<String>
    )
}
