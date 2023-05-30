package com.jetsunnalgonda.good_drinks

//import RandomActivity
//import SearchDrinksActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jetsunnalgonda.good_drinks.ui.theme.GoodDrinksTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Locale
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var randomButton: Button
    private lateinit var browseButton: Button
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomButton = findViewById(R.id.randomButton)
        browseButton = findViewById(R.id.browseButton)
        searchButton = findViewById(R.id.searchButton)
        randomButton.setOnClickListener {
            print("random button pressed")
            val intent = Intent(this, RandomActivity::class.java)
            startActivity(intent)

        }
        browseButton.setOnClickListener {
            print("browse button pressed")
            val intent = Intent(this, BrowseDrinksActivity::class.java)
            startActivity(intent)

        }
        searchButton.setOnClickListener {
            print("search button pressed")
            val intent = Intent(this, SearchDrinksActivity::class.java)
            startActivity(intent)

        }

//        setContent {
//            GoodDrinksTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
//            }
//        }
    }
}


//data class Drink(
////    val id: Int?,
//    val name: String?,
////    val ingredients: List<String>,
//    val glass: String?,
//    val instructions: String?,
//)




data class Drink(
    val id: Int,
    val name: String?,
    val glass: String?,
    val instructions: String?,
    val ingredients: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(glass)
        parcel.writeString(instructions)
        parcel.writeString(ingredients)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Drink> {
        override fun createFromParcel(parcel: Parcel): Drink {
            return Drink(parcel)
        }

        override fun newArray(size: Int): Array<Drink?> {
            return arrayOfNulls(size)
        }
    }

    fun getImageUrl(): String {
        var imageName = name?.replace(" ", "_")?.lowercase(Locale.getDefault())
        if (imageName == "piña_colada") imageName = "pina_colada"
        Log.e(TAG, " imageName = $imageName ")
        return "$BASE_IMG_URL$imageName.jpg"

    }
}
