package com.jetsunnalgonda.good_drinks

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bumptech.glide.Glide

class DrinkDetailsActivity : AppCompatActivity() {

    private lateinit var drink: Drink

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drink_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drink = intent.getParcelableExtra(EXTRA_DRINK)!!

        val drinkNameTextView: TextView = findViewById(R.id.drinkName)
        val drinkGlassTextView: TextView = findViewById(R.id.glassType)
        val drinkInstructionsTextView: TextView = findViewById(R.id.instructionsText)
        val drinkIngredientsTextView: TextView = findViewById(R.id.ingredients)
        val drinkImageView: ImageView = findViewById(R.id.drinkImage)
//        val drinkIngredientsTextView: TextView = findViewById(R.id.drinkIngredients)

        drinkNameTextView.text = drink.name
        drinkGlassTextView.text = drink.glass
        drinkInstructionsTextView.text = drink.instructions
        drinkIngredientsTextView.text = drink.ingredients


        Glide.with(this)
            .load(drink.getImageUrl())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(drinkImageView)

    }

    companion object {
        const val EXTRA_DRINK = "extra.DRINK"
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
