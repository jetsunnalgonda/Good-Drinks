package com.jetsunnalgonda.good_drinks

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import android.content.Context


interface RandomApiService {
    @GET("drinks/random")
    fun getRandomDrink(): Call<Drink>
}

class RandomActivity : AppCompatActivity() {

    private lateinit var randomDrinkAdapter: RandomDrinkAdapter

    private val apiService: RandomApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://nalgonda.pythonanywhere.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_drink)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drinkImage: ImageView = findViewById(R.id.drinkImage)
        val drinkName: TextView = findViewById(R.id.drinkName)
        val glassType: TextView = findViewById(R.id.glassType)
        val ingredients: TextView = findViewById(R.id.ingredients)
        val instructionsText: TextView = findViewById(R.id.instructionsText)

        randomDrinkAdapter = RandomDrinkAdapter(this)
        randomDrinkAdapter.initializeViews(drinkImage, drinkName, glassType, ingredients, instructionsText)

        fetchRandomDrink()
    }

    private fun fetchRandomDrink() {
        val call: Call<Drink> = apiService.getRandomDrink()
        call.enqueue(object : Callback<Drink> {
            override fun onResponse(call: Call<Drink>, response: Response<Drink>) {
                if (response.isSuccessful) {
                    val drink: Drink? = response.body()
                    if (drink != null) {
                        randomDrinkAdapter.bindData(drink)
                    }
                } else {
                    // Handle API call failure
                    Log.e(TAG, "Failed to fetch random drink: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Drink>, t: Throwable) {
                // Handle API call failure
                Log.e(TAG, "Failed to fetch random drink", t)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "RandomActivity"
    }
}

class RandomDrinkAdapter(private val context: Context) {

    private lateinit var drinkImage: ImageView
    private lateinit var drinkName: TextView
    private lateinit var glassType: TextView
    private lateinit var ingredients: TextView
    private lateinit var instructionsText: TextView

    fun initializeViews(drinkImage: ImageView, drinkName: TextView, glassType: TextView, ingredients: TextView, instructionsText: TextView) {
        this.drinkImage = drinkImage
        this.drinkName = drinkName
        this.glassType = glassType
        this.ingredients = ingredients
        this.instructionsText = instructionsText
    }

    fun bindData(drink: Drink) {
        drinkName.text = drink.name
        glassType.text = drink.glass
        ingredients.text = drink.ingredients
        instructionsText.text = drink.instructions

        Glide.with(context)
            .load(drink.getImageUrl())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(drinkImage)
    }
}
