package com.jetsunnalgonda.good_drinks

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.jetsunnalgonda.good_drinks.Drink
import com.jetsunnalgonda.good_drinks.DrinkDetailsActivity
import com.jetsunnalgonda.good_drinks.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//data class Drink(val id: Int, val name: String, val imageUrl: String)

interface DrinkApiService {
    @GET("drinks")
    fun getDrinks(): Call<List<Drink>>
}

class SearchDrinkAdapter(private val drinks: List<Drink>, private val listeners: DrinkListeners): BaseAdapter() {
    override fun getCount(): Int {
        return drinks.size
    }

    override fun getItem(position: Int): Any {
        return drinks[position]
    }

    override fun getItemId(position: Int): Long {
        // You can return a unique identifier for each item if needed
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_drink, parent, false)

        val imageView: ImageView = view.findViewById(R.id.drinkImage)
        val nameTextView: TextView = view.findViewById(R.id.drinkName)

        val drink = drinks[position]

        Glide.with(view)
            .load(drink.getImageUrl())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(imageView)

        nameTextView.text = drink.name

        view.setOnClickListener {
            val intent = Intent(view.context, DrinkDetailsActivity::class.java)
            intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
            view.context.startActivity(intent)
        }
        imageView.setOnClickListener {
            listeners.onDrinkClicked(drink)
        }

        return view
    }

}

//class SearchDrinkAdapter(private val drinks: List<Drink>) : RecyclerView.Adapter<SearchDrinkAdapter.ViewHolder>(),
//    ListAdapter {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.drinkImage)
//        val nameTextView: TextView = itemView.findViewById(R.id.drinkName)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_drink, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val drink = drinks[position]
//
//        Glide.with(holder.itemView)
//            .load(drink.getImageUrl())
//            .placeholder(R.drawable.placeholder_image)
//            .error(R.drawable.error_image)
//            .into(holder.imageView)
//
//        holder.nameTextView.text = drink.name
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, DrinkDetailsActivity::class.java)
//            intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
//            holder.itemView.context.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return drinks.size
//    }
//}

class SearchDrinksActivity : AppCompatActivity(), DrinkListeners {

    private lateinit var searchView: SearchView
    private lateinit var gridView: GridView
    private lateinit var adapter: SearchDrinkAdapter

    private lateinit var drinks: List<Drink>

    private val apiService: DrinkApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrinkApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_drinks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchView = findViewById(R.id.searchView)
        gridView = findViewById(R.id.gridView)

        adapter = SearchDrinkAdapter(emptyList(), this)
        gridView.adapter = adapter
//        recyclerView.layoutManager = GridLayoutManager(this, 2) //, LinearLayoutManager.HORIZONTAL, false) // Set the number of columns as desired

//        val spanCount = 2 // Number of columns
//        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
//        recyclerView.layoutManager = layoutManager

        // Fetch drinks from the API
        fetchDrinks()

//        filterDrinks("", emptyList())

        // Set up search listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterDrinks(newText, drinks)
                return true
            }
        })

    }

    private fun fetchDrinks() {

        val call: Call<List<Drink>> = apiService.getDrinks()
        call.enqueue(object : Callback<List<Drink>> {
            override fun onResponse(call: Call<List<Drink>>, response: Response<List<Drink>>) {
                if (response.isSuccessful) {
                    drinks = response.body() ?: emptyList()
                    drinks?.let {
                        updateDrinkList(it)
                    }
                } else {
                    // Handle API error
                    Log.e(TAG, "Failed to fetch drinks: ${response.code()}")
                    drinks = emptyList()
                }
//                displayAllDrinks()
            }

            override fun onFailure(call: Call<List<Drink>>, t: Throwable) {
                // Handle network failure
                Log.e(TAG, "Failed to fetch drinks", t)
                drinks = emptyList()
//                displayAllDrinks()
            }
        })

//        adapter = SearchDrinkAdapter(drinks)
//        recyclerView.adapter = adapter
    }

    private fun updateDrinkList(drinks: List<Drink>) {
        adapter = SearchDrinkAdapter(drinks, this)
        gridView.adapter = adapter
//        filterDrinks("", drinks)
    }

    private fun filterDrinks(query: String, allDrinks: List<Drink>) {
//        if (query.isNotEmpty())
//            recyclerView.layoutManager = GridLayoutManager(this, 2)
//        else
//            recyclerView.layoutManager = GridLayoutManager(this, 0)
//        val filteredDrinks = if (query.isNotEmpty()) {
//            allDrinks.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//        } else {
//            emptyList()
//        }
//        val filteredDrinks: List<Drink> = emptyList()
        val filteredDrinks = allDrinks.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredDrinks)
//        listView.adapter = adapter
        val newAdapter = SearchDrinkAdapter(filteredDrinks, this)
        gridView.adapter = newAdapter
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDrinkClicked(drink: Drink) {
        Log.e(BrowseDrinksActivity.TAG,"")
        Log.e(BrowseDrinksActivity.TAG,"onDrinkClicked")
        Log.e(BrowseDrinksActivity.TAG,"")
        val intent = Intent(this, DrinkDetailsActivity::class.java)
        intent.putExtra(DrinkDetailsActivity.EXTRA_DRINK, drink)
        startActivity(intent)

    }
}
