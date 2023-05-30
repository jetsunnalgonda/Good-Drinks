package com.jetsunnalgonda.good_drinks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
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
import timber.log.Timber

interface BrowseApiService {
    @GET("drinks")
    fun getDrinks(): Call<List<Drink>>
}

interface DrinkListeners {
    fun onDrinkClicked(drink: Drink)
}

class DrinkAdapter(private val drinks: List<Drink>, private val listeners: DrinkListeners) : BaseAdapter() {
//class DrinkAdapter(private val drinks: List<Drink>) : BaseAdapter() {



    override fun getCount(): Int {
        return drinks.size
    }

    override fun getItem(position: Int): Any {
        return drinks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.grid_item_drink, parent, false)

        val drink = drinks[position]

        val imageView = view.findViewById<ImageView>(R.id.drinkImage)
        val textView = view.findViewById<TextView>(R.id.drinkName)

//        imageView.setOnItemClickListener {
//            listeners.onDrinkClicked(drinks[position])
//        }

        Glide.with(view)
            .load(drink.getImageUrl())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageView)

//        imageView.viewTreeObserver.addOnGlobalLayoutListener {
//            val width = imageView.width
//            val height = imageView.height
//
//            GlideApp.with(imageView)
//                .load(drink.getImageUrl())
//                .override(width, height)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .into(imageView)
//        }
        // Set the drink name
        textView.text = drink.name

        // Reset the height of the ImageView to make it square
//        val width = parent?.width ?: 0
//        val itemSize = width / gridView.numColumns
//        imageView.layoutParams.height = itemSize

        imageView.setOnClickListener {
            listeners.onDrinkClicked(drink)
        }

        return view
    }

}


class BrowseDrinksActivity : AppCompatActivity(), DrinkListeners {

    private lateinit var gridView: GridView
    private lateinit var drinks: List<Drink>

    private val apiService: BrowseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrowseApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browse_drinks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gridView = findViewById(R.id.gridView)
        Log.e(TAG,"")
        Log.e(TAG,"onCreate")
        Log.e(TAG,"")

        fetchDrinks()
    }

    private fun fetchDrinks() {
        val call: Call<List<Drink>> = apiService.getDrinks()
        call.enqueue(object : Callback<List<Drink>> {
            override fun onResponse(call: Call<List<Drink>>, response: Response<List<Drink>>) {
                if (response.isSuccessful) {
                    drinks = response.body() ?: emptyList()
                    displayDrinks(drinks)
                } else {
                    Log.e(TAG, "Failed to fetch drinks: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Drink>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch drinks", t)
            }
        })
    }

    private fun displayDrinks(drinks: List<Drink>) {
        val adapter = DrinkAdapter(drinks,this)
        gridView.adapter = adapter

    }

    private fun showDrinkDetails(drink: Drink) {
        val intent = Intent(this, DrinkDetailsActivity::class.java)
        intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "BrowseDrinksActivity"
    }

    override fun onDrinkClicked(drink: Drink) {
        Log.e(TAG,"")
        Log.e(TAG,"onDrinkClicked")
        Log.e(TAG,"")
        val intent = Intent(this, DrinkDetailsActivity::class.java)
        intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
        startActivity(intent)

    }
}