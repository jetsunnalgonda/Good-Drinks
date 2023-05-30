package com.jetsunnalgonda.good_drinks

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jetsunnalgonda.good_drinks.R.id

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("drinks/random") // Replace with your actual endpoint for getting a random drink
    fun getRandomDrink(): Call<Drink>
}

class RandomActivity : AppCompatActivity() {

    private lateinit var drinkImageView: ImageView
    private lateinit var drinkNameTextView: TextView
    private lateinit var glassTypeTextView: TextView
    private lateinit var instructionsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_drink)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize views
        drinkImageView = findViewById(R.id.drinkImage)
        drinkNameTextView = findViewById(R.id.drinkName)
        glassTypeTextView = findViewById(R.id.glassType)
        instructionsTextView = findViewById(R.id.instructionsText)

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("http://nalgonda.pythonanywhere.com/api/") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Make API request to get random drink data
        val randomDrinkCall = apiService.getRandomDrink()
        randomDrinkCall.enqueue(object : Callback<Drink> {
            override fun onResponse(call: Call<Drink>, response: Response<Drink>) {
                if (response.isSuccessful) {
                    val randomDrink = response.body()
                    if (randomDrink != null) {
                        // Extract the drink details
                        val drinkName = randomDrink.name
                        val glassType = randomDrink.glass
                        val instructions = randomDrink.instructions
                        val imageUrl = randomDrink.getImageUrl()

                        // Load the drink image using Glide or any other image loading library
                        Glide.with(this@RandomActivity)
                            .load(imageUrl)
                            .into(drinkImageView)

                        // Set the drink name, glass type, and instructions
                        drinkNameTextView.text = drinkName
                        glassTypeTextView.text = glassType
                        instructionsTextView.text = instructions
                    }
                }
            }

            override fun onFailure(call: Call<Drink>, t: Throwable) {
                // Handle API call failure
            }
        })

        // Get the random drink data from your API or local storage
//        val drinkName = "Mojito" // Replace with the actual drink name
//        val glassType = "Collins glass" // Replace with the actual glass type
//        val instructions = "Muddle mint leaves with lime juice and simple syrup. Add rum and ice. Top with soda water. Garnish with mint sprig." // Replace with the actual instructions

        // Load the drink image using Picasso or any other image loading library
//        val imageUrl = "https://nalgonda.pythonanywhere.com/api/drinks/1" // Replace with the actual image URL
//        Picasso.get().load(imageUrl).into(drinkImageView)
//        Glide.with(this)
//            .load(imageUrl)
//            .into(drinkImageView)

        // Set the drink name, glass type, and instructions
//        drinkNameTextView.text = drinkName
//        glassTypeTextView.text = glassType
//        instructionsTextView.text = instructions
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


//package com.jetsunnalgonda.good_drinks
//
//import android.os.Bundle
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.ImageView
//import android.widget.TextView
//import com.bumptech.glide.Glide
//import okhttp3.ResponseBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//
//
////interface ApiService {
////    @GET("drinks/random")
////    suspend fun getRandomDrink(): ResponseBody
////}
//
//interface ApiService {
//    @GET("api/random") // Replace with your actual endpoint for getting a random drink
//    fun getRandomDrink(): Call<Drink>
//}
//
//class RandomActivity : AppCompatActivity() {
//
//    private lateinit var drinkImageView: ImageView
//    private lateinit var drinkNameTextView: TextView
//    private lateinit var glassTypeTextView: TextView
//    private lateinit var instructionsTextView: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.random_drink)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        // Initialize views
//        drinkImageView = findViewById(R.id.drinkImage)
//        drinkNameTextView = findViewById(R.id.drinkName)
//        glassTypeTextView = findViewById(R.id.glassType)
//        instructionsTextView = findViewById(R.id.instructionsText)
//
//        // Retrofit setup
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://nalgonda.pythonanywhere.com/api/") // Replace with your API base URL
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//
//        // Make API request to get random drink data
//        val randomDrinkCall = apiService.getRandomDrink()
//        randomDrinkCall.enqueue(object : Callback<Drink> {
//            override fun onResponse(call: Call<Drink>, response: Response<Drink>) {
//                if (response.isSuccessful) {
//                    val randomDrink = response.body()
//                    if (randomDrink != null) {
//                        // Extract the drink details
//                        val drinkName = randomDrink.name
//                        val glassType = randomDrink.glassType
//                        val instructions = randomDrink.instructions
//                        val imageUrl = "" // randomDrink.imageUrl
//
//                        // Load the drink image using Glide or any other image loading library
//                        Glide.with(this@RandomActivity)
//                            .load(imageUrl)
//                            .into(drinkImageView)
//
//                        // Set the drink name, glass type, and instructions
//                        drinkNameTextView.text = drinkName
//                        glassTypeTextView.text = glassType
//                        instructionsTextView.text = instructions
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Drink>, t: Throwable) {
//                // Handle API call failure
//            }
//        })
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//}
