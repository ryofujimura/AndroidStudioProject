package com.zybooks.finalproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RestaurantDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_description)

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val restaurants = gson.fromJson(responseBody, MainActivity.YelpApiResponse::class.java)

                runOnUiThread {
                    val adapter = object : ArrayAdapter<MainActivity.Business>(this@RestaurantDescriptionActivity, R.layout.list_item_restaurant, restaurants.businesses) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val itemView = layoutInflater.inflate(R.layout.list_item_restaurant, parent, false)
                            val restaurant = getItem(position)

                            val restaurantNameTextView = itemView.findViewById<TextView>(R.id.restaurantNameTextView)
                            val restaurantImageView = itemView.findViewById<ImageView>(R.id.restaurantImageView)

                            if (restaurant != null) {
                                restaurantNameTextView.text = restaurant.name
                            }
                            if (restaurant != null) {
                                Glide.with(this@RestaurantDescriptionActivity).load(restaurant.image_url).into(restaurantImageView)
                            }

                            return itemView
                        }
                    }
                    restaurantImageView1.adapter = adapter
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}
