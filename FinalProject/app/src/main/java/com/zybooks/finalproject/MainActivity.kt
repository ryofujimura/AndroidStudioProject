package com.zybooks.finalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchMoreButton: Button
    private lateinit var restaurantListView: ListView

    private val yelpApiKey = "110fzSdHM6bFShApZ7HVpEFw6hJwfNBiXlfp4lSL4u6GU64MJOJxLUOFkHx2VnezuCHHNtnTFbQPkuDjMkHabmHCV4tM2NV2UZRssuuFcJlqVXn9107TajsE8pu7YXYx"
    private val client = OkHttpClient()
    private val gson = Gson()

    companion object {
        lateinit var restaurants: YelpApiResponse
        var currentPage = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationEditText = findViewById(R.id.locationEditText)
        searchButton = findViewById(R.id.searchButton)
        searchMoreButton = findViewById(R.id.searchMoreButton)
        restaurantListView = findViewById(R.id.restaurantListView)

        searchButton.setOnClickListener {
            currentPage = 0 // Reset the current page when performing a new search
            val location = locationEditText.text.toString()
            searchRestaurants(location)

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        searchMoreButton.setOnClickListener {
            currentPage++
            val location = locationEditText.text.toString()
            searchRestaurants(location, currentPage * 10) // Fetch the next page of results
        }

        restaurantListView.setOnItemClickListener { _, _, position, _ ->
            val selectedRestaurantJson = gson.toJson(restaurants.businesses[position])

            val intent = Intent(this, RestaurantDescriptionActivity::class.java)
            intent.putExtra("selectedRestaurant", selectedRestaurantJson)
            startActivity(intent)
        }
    }

    private fun searchRestaurants(location: String, offset: Int = 0) {
        val url = "https://api.yelp.com/v3/businesses/search?term=restaurant&location=$location&limit=10&offset=$offset"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $yelpApiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                restaurants = gson.fromJson(responseBody, YelpApiResponse::class.java)
                runOnUiThread {
                    val restaurantNames = restaurants.businesses.map { it.name }
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, restaurantNames)
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
        val address: String,
        val description: String,
        val images: List<String>
    )
}