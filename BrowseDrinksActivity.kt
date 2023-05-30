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

interface BrowseApiService {
    @GET("drinks")
    fun getDrinks(): Call<List<Drink>>
}


class DrinkAdapter(private val drinks: List<Drink>) : BaseAdapter() {

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

        // Load the image using the provided URL
        val imageUrl = "http://nalgonda.pythonanywhere.com/api/drinks/${drink.id}"
//        Glide.with(imageView)
//            .asBitmap()
//            .load(imageUrl)
//            .into(object : CustomTarget<Bitmap>() {
//                fun onResourceReady(resource: Bitmap, transition: Transition?) {
//                    // Create a BitmapDrawable from the loaded bitmap
//                    val drawable = BitmapDrawable(view.context.resources, resource)
//
//                    // Set the drawable as the image in the ImageView
//                    imageView.setImageDrawable(drawable)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // Handle any cleanup or placeholder logic here
//                }
//            })
//        Glide.with(imageView)
//            .asBitmap()
//            .load(drink.getImageUrl())
//            .into(object : CustomTarget<Bitmap>(){
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    imageView.setImageBitmap(resource)
//                }
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // this is called when imageView is cleared on lifecycle call or for
//                    // some other reason.
//                    // if you are referencing the bitmap somewhere else too other than this imageView
//                    // clear it here as you can no longer have the bitmap
//                }
//            })

        Glide.with(imageView)
//        .load(drink.getImageUrl())
            .load("http://www1.gantep.edu.tr/~halukisik/api/images/cosmopolitan.jpg")
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageView)

        imageView.viewTreeObserver.addOnGlobalLayoutListener {
            val width = imageView.width
            val height = imageView.height

            GlideApp.with(imageView)
                .load(drink.getImageUrl())
//                .load("http://www1.gantep.edu.tr/~halukisik/api/images/cosmopolitan.jpg")
                .override(width, height)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)
        }
        // Set the drink name
        textView.text = drink.name

        return view
    }


//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view = convertView ?: LayoutInflater.from(parent?.context)
//            .inflate(R.layout.grid_item_drink, parent, false)
//
//        val drink = drinks[position]
//
//        val imageView = view.findViewById<ImageView>(R.id.drinkImage)
//        val textView = view.findViewById<TextView>(R.id.drinkName)
//
//        // Calculate the desired image height based on the width
//        val columnWidth = (parent as GridView).columnWidth
//        val aspectRatio = 1.0f // Assuming the aspect ratio of the images is 1:1
//        val imageHeight = (columnWidth * aspectRatio).toInt()
//
//        // Set the calculated height for the image view
//        val layoutParams = imageView.layoutParams
//        layoutParams.height = imageHeight
//        imageView.layoutParams = layoutParams
//
//        // Modify the image name to match the URL format
//        val imageName = drink.name?.replace(" ", "_")?.lowercase(Locale.ROOT)
//        val imageUrl = "http://nalgonda.pythonanywhere.com/api/images/$imageName.jpg"
//        print(imageUrl)
//
//
////        val imageUrl = "http://nalgonda.pythonanywhere.com/api/drinks/image" + drink.id
//        // Load the drink image using Glide or any other image loading library
//        Glide.with(imageView)
//            .load(drink.getImageUrl())
//            .placeholder(R.drawable.placeholder_image)
//            .error(R.drawable.error_image)
//            .into(imageView)
//
////        imageView.viewTreeObserver.addOnGlobalLayoutListener {
////            val width = imageView.width
////            val height = imageView.height
////
////            Glide.with(imageView)
////                .load(imageUrl)
////                .override(width, height)
////                .placeholder(R.drawable.placeholder_image)
////                .error(R.drawable.error_image)
////                .into(imageView)
////        }
//
//
//        // Set the drink name
//        textView.text = drink.name
//
//        return view
//    }
}


class BrowseDrinksActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var drinks: List<Drink>

    private val apiService: BrowseApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://nalgonda.pythonanywhere.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrowseApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.browse_drinks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gridView = findViewById(R.id.gridView)
//        gridView.onItemClickListener =
//            AdapterView.OnItemClickListener { _, _, position, _ ->
//                val drink = drinks[position]
//                showDrinkDetails(drink)
//            }
//        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            val drink = drinks[position]
//            val intent = Intent(this, DrinkDetailsActivity::class.java)
//            intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
//            startActivity(intent)
//        }

        // Implement OnItemClickListener
//        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val selectedDrink = drinks[position] // Assuming `drinks` is your list of drinks
//
//            val intent = Intent(this@BrowseDrinksActivity, DrinkDetailsActivity::class.java)
//            intent.putExtra("drink", selectedDrink.id)
//            startActivity(intent)
//        }

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
        val adapter = DrinkAdapter(drinks)
        gridView.adapter = adapter
        gridView.post {
            gridView.setOnItemClickListener { parent, view, position, id ->
                val selectedDrink = drinks[position] // Assuming `drinks` is your list of drinks

                val intent = Intent(this, DrinkDetailsActivity::class.java)
                intent.putExtra("drink", selectedDrink)
                startActivity(intent)
            }
        }
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
        private const val TAG = "BrowseDrinksActivity"
    }
}




















//package com.jetsunnalgonda.good_drinks
//
//import android.app.AlertDialog
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.os.Bundle
////import android.transition.Transition
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.GridView
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.graphics.drawable.toBitmap
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.CustomTarget
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import java.util.Locale
//
//import com.bumptech.glide.request.transition.Transition



//interface BrowseApiService {
//    @GET("drinks")
//    fun getDrinks(): Call<List<Drink>>
//}

//class DrinkAdapter(private val drinks: List<Drink>) : BaseAdapter() {
//
//    override fun getCount(): Int {
//        return drinks.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return drinks[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view = convertView ?: LayoutInflater.from(parent?.context)
//            .inflate(R.layout.grid_item_drink, parent, false)
//
//        val drink = drinks[position]
//
//        val imageView = view.findViewById<ImageView>(R.id.drinkImage)
//        val textView = view.findViewById<TextView>(R.id.drinkName)
//
//        // Load the image using the provided URL
//        val imageUrl = "http://nalgonda.pythonanywhere.com/api/drinks/${drink.id}"
////        Glide.with(imageView)
////            .asBitmap()
////            .load(imageUrl)
////            .into(object : CustomTarget<Bitmap>() {
////                fun onResourceReady(resource: Bitmap, transition: Transition?) {
////                    // Create a BitmapDrawable from the loaded bitmap
////                    val drawable = BitmapDrawable(view.context.resources, resource)
////
////                    // Set the drawable as the image in the ImageView
////                    imageView.setImageDrawable(drawable)
////                }
////
////                override fun onLoadCleared(placeholder: Drawable?) {
////                    // Handle any cleanup or placeholder logic here
////                }
////            })
////        Glide.with(imageView)
////            .asBitmap()
////            .load(drink.getImageUrl())
////            .into(object : CustomTarget<Bitmap>(){
////                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
////                    imageView.setImageBitmap(resource)
////                }
////                override fun onLoadCleared(placeholder: Drawable?) {
////                    // this is called when imageView is cleared on lifecycle call or for
////                    // some other reason.
////                    // if you are referencing the bitmap somewhere else too other than this imageView
////                    // clear it here as you can no longer have the bitmap
////                }
////            })
//
//        Glide.with(imageView)
////        .load(drink.getImageUrl())
//            .load("http://www1.gantep.edu.tr/~halukisik/api/images/cosmopolitan.jpg")
//            .placeholder(R.drawable.placeholder_image)
//            .error(R.drawable.error_image)
//            .into(imageView)
//
//        imageView.viewTreeObserver.addOnGlobalLayoutListener {
//            val width = imageView.width
//            val height = imageView.height
//
//            GlideApp.with(imageView)
//                .load(drink.getImageUrl())
////                .load("http://www1.gantep.edu.tr/~halukisik/api/images/cosmopolitan.jpg")
//                .override(width, height)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .into(imageView)
//        }
//        // Set the drink name
//        textView.text = drink.name
//
//        return view
//    }
//
//
////    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
////        val view = convertView ?: LayoutInflater.from(parent?.context)
////            .inflate(R.layout.grid_item_drink, parent, false)
////
////        val drink = drinks[position]
////
////        val imageView = view.findViewById<ImageView>(R.id.drinkImage)
////        val textView = view.findViewById<TextView>(R.id.drinkName)
////
////        // Calculate the desired image height based on the width
////        val columnWidth = (parent as GridView).columnWidth
////        val aspectRatio = 1.0f // Assuming the aspect ratio of the images is 1:1
////        val imageHeight = (columnWidth * aspectRatio).toInt()
////
////        // Set the calculated height for the image view
////        val layoutParams = imageView.layoutParams
////        layoutParams.height = imageHeight
////        imageView.layoutParams = layoutParams
////
////        // Modify the image name to match the URL format
////        val imageName = drink.name?.replace(" ", "_")?.lowercase(Locale.ROOT)
////        val imageUrl = "http://nalgonda.pythonanywhere.com/api/images/$imageName.jpg"
////        print(imageUrl)
////
////
//////        val imageUrl = "http://nalgonda.pythonanywhere.com/api/drinks/image" + drink.id
////        // Load the drink image using Glide or any other image loading library
////        Glide.with(imageView)
////            .load(drink.getImageUrl())
////            .placeholder(R.drawable.placeholder_image)
////            .error(R.drawable.error_image)
////            .into(imageView)
////
//////        imageView.viewTreeObserver.addOnGlobalLayoutListener {
//////            val width = imageView.width
//////            val height = imageView.height
//////
//////            Glide.with(imageView)
//////                .load(imageUrl)
//////                .override(width, height)
//////                .placeholder(R.drawable.placeholder_image)
//////                .error(R.drawable.error_image)
//////                .into(imageView)
//////        }
////
////
////        // Set the drink name
////        textView.text = drink.name
////
////        return view
////    }
//}

//class BrowseDrinksActivity : AppCompatActivity() {
//
//    private lateinit var gridView: GridView
//
//    // Create an instance of the BrowseApiService
//    private val apiService: BrowseApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://nalgonda.pythonanywhere.com/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(BrowseApiService::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.browse_drinks)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        gridView = findViewById(R.id.gridView)
//
//        // Call the method to fetch drinks from the API
//        fetchDrinks()
//    }
//
//    private fun fetchDrinks() {
//        // Make the API call using the BrowseApiService
//        val call: Call<List<Drink>> = apiService.getDrinks()
//        call.enqueue(object : Callback<List<Drink>> {
//            override fun onResponse(call: Call<List<Drink>>, response: Response<List<Drink>>) {
//                if (response.isSuccessful) {
//                    val drinks: List<Drink>? = response.body()
//                    if (drinks != null) {
//                        // Handle the list of drinks
//                        displayDrinks(drinks)
//                    }
//                } else {
//                    // Handle API call failure
//                    Log.e(TAG, "Failed to fetch drinks: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Drink>>, t: Throwable) {
//                // Handle API call failure
//                Log.e(TAG, "Failed to fetch drinks", t)
//            }
//        })
//    }
//
//    private fun displayDrinks(drinks: List<Drink>) {
//        // Create an instance of the DrinkAdapter
//        val adapter = DrinkAdapter(drinks)
//
//        // Set the adapter to the GridView
//        gridView.adapter = adapter
//    }
//
//    companion object {
//        private const val TAG = "BrowseDrinksActivity"
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


//package com.jetsunnalgonda.good_drinks
//
//import android.app.AlertDialog
//import android.app.Application
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.GridView
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//
//import retrofit2.Callback
//import retrofit2.Response
//import timber.log.Timber
//
//
////data class Drink(
////    val name: String,
////    val imageUrl: String
////)
//interface BrowseApiService {
//    @GET("drinks")
//    fun getDrinks(): Call<List<Drink>>
//}
//
//class DrinkAdapter(private val drinks: List<Drink>) : BaseAdapter() {
//
//    override fun getCount(): Int {
//        return drinks.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return drinks[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view = convertView ?: LayoutInflater.from(parent?.context)
//            .inflate(R.layout.grid_item_drink, parent, false)
//
//        val drink = drinks[position]
//
//        val imageView = view.findViewById<ImageView>(R.id.drinkImage)
//        val textView = view.findViewById<TextView>(R.id.drinkName)
//
//
//        // Load the drink image using Glide or any other image loading library
//        Glide.with(imageView)
//            .load(drink.name)
//            .into(imageView)
//
//        // Set the drink name
//        textView.text = drink.name
//
//        return view
//    }
//}
//
//class BrowseDrinksActivity : AppCompatActivity() {
//
//    // Create an instance of the BrowseApiService
//    private val apiService: BrowseApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://nalgonda.pythonanywhere.com/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(BrowseApiService::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.browse_drinks)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
////        class YourApplication : Application() {
////            override fun onCreate() {
////                super.onCreate()
////
////                if (BuildConfig.DEBUG) {
////                    Timber.plant(Timber.DebugTree())
////                }
////            }
////        }
//
//        // Call the method to fetch drinks from the API
//        fetchDrinks()
//    }
//
//    private fun fetchDrinks() {
//        // Make the API call using the BrowseApiService
//        val call: Call<List<Drink>> = apiService.getDrinks()
//        call.enqueue(object : Callback<List<Drink>> {
//            override fun onResponse(call: Call<List<Drink>>, response: Response<List<Drink>>) {
//                if (response.isSuccessful) {
//                    val drinks: List<Drink>? = response.body()
//
//                    println(response.raw().toString())
//                    // Show a dialog with the raw response data
//                    val dialogBuilder = AlertDialog.Builder(this@BrowseDrinksActivity)
//                    dialogBuilder.setTitle("Raw Response Data")
//                    dialogBuilder.setMessage(drinks.toString())
//                    dialogBuilder.setPositiveButton("OK") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    val dialog = dialogBuilder.create()
//                    dialog.show()
//
////                    Timber.d("Raw Response: $rawResponse")
//                    if (drinks != null) {
//                        // Handle the list of drinks
//                        displayDrinks(drinks)
//                    }
//                } else {
//                    // Handle API call failure
//                    Log.e(TAG, "Failed to fetch drinks: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Drink>>, t: Throwable) {
//                // Handle API call failure
//                Log.e(TAG, "Failed to fetch drinks", t)
//            }
//        })
//    }
//
//    private fun displayDrinks(drinks: List<Drink>) {
//        // Update your UI to display the list of drinks
//        // For example, populate a RecyclerView or GridView with the drink data
//    }
//
//    companion object {
//        private const val TAG = "BrowseDrinksActivity"
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//}