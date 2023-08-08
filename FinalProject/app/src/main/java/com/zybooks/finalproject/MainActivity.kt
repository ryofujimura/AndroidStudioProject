package com.zybooks.finalproject
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


class MainActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var restaurantListView: ListView

    private val yelpApiKey = "110fzSdHM6bFShApZ7HVpEFw6hJwfNBiXlfp4lSL4u6GU64MJOJxLUOFkHx2VnezuCHHNtnTFbQPkuDjMkHabmHCV4tM2NV2UZRssuuFcJlqVXn9107TajsE8pu7YXYx" // Update this line
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationEditText = findViewById(R.id.locationEditText)
        searchButton = findViewById(R.id.searchButton)
        restaurantListView = findViewById(R.id.restaurantListView)

        // Your search button click listener
        searchButton.setOnClickListener {
            val location = locationEditText.text.toString()
            searchRestaurants(location)

            // Hide the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        // This is where you set the item click listener for the restaurantListView
        restaurantListView.setOnItemClickListener { parent, view, position, id ->
            val selectedRestaurant = (restaurantListView.adapter as ArrayAdapter<String>).getItem(position)

            val intent = Intent(this, RestaurantDescriptionActivity::class.java)
            intent.putExtra("selectedRestaurant", selectedRestaurant)
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
                val restaurants = gson.fromJson(responseBody, YelpApiResponse::class.java)
                runOnUiThread {
                    // Populate ListView with restaurant names or other data
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
    data class Business(val name: String, val address: String, val images: List<String>)
}
