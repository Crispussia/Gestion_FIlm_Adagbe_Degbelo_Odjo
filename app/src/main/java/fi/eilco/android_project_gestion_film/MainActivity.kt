package fi.eilco.android_project_gestion_film

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.fragments.FavoriteFragment
import fi.eilco.android_project_gestion_film.fragments.HomeFragment
import fi.eilco.android_project_gestion_film.user_data.SharedPrefHelper


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPrefHelper: SharedPrefHelper
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectButton = findViewById<ImageView>(R.id.connect)
        //charger notre plant repository
        //injecter le fragment dans la boite (fragment_container)
        //val s=findViewById<SearchView>(R.id.movie_search)

        val username = findViewById<TextView>(R.id.textuser)
        val favorisButton = findViewById<ImageView>(R.id.favoris)
        //val favorisButtonUser = findViewById<ImageView>(R.id.favoris_user_page)
        val textbadge = findViewById<TextView>(R.id.textbadge)
        //val connectValidButton = findViewById<ImageView>(R.id.connectValide)
        var intentUser = intent.getStringExtra("username")
        val repo = FilmRepository()
        //textAnimals.text="$intentAnimals"
        Log.d("SucessIntent", intentUser.toString())

        if (intentUser.toString() != "null") {
            Log.d("SucessIntent", intentUser.toString())

            //connectButton.visibility = View.GONE
            connectButton.setImageResource(R.drawable.ic_account_true)
            //connectValidButton.visibility = View.VISIBLE
            favorisButton.setImageResource(R.drawable.ic_heart_filled_red);
            username.text = "$intentUser"
            //mettre à jour la liste de plantes
            repo.updateFavorisImage(username, favorisButton, textbadge)

        }

        if (username.text.toString()==""){
            connectButton.setImageResource(R.drawable.ic_account_false)
        }
        //Si on est sur la page d'accueil sans connection de l'utilisateur


        favorisButton.setOnClickListener {
            if (username.text.toString() == "") {
                Toast.makeText(
                    this@MainActivity,
                    "Please connect to see your favorite list",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                val intentRegister = Intent(this@MainActivity, FavoriteActivity::class.java)
                intentRegister.apply {
                    putExtra("username",username.text.toString())
                    startActivity(this)
                }
            }
        }

        /* BEGINNING SharedPreferences Testing */
        sharedPrefHelper = SharedPrefHelper(this)
        sharedPrefHelper.putString("session_variable", "SharedPreferences WORK !!!")// -1 is returned when the session variable is not found
        val sess_var = sharedPrefHelper.getString("session_variable")
        Log.d("Session Variable testing", "Main_SESSIONNNNNNNNN " + sess_var)
        /* END SharedPreferences Testing */

        // Here we handle the case where a new activity_main has been launched when clicking on
        // one of the item of the navigationView
        val id = intent.getStringExtra("id")
        val currentGenre = intent.getStringExtra("current_genre")

        if (currentGenre != null) {
            // We set the new current genre on the activity_main
            this.findViewById<TextView>(R.id.current_genre).text = currentGenre
        }


        val transaction = supportFragmentManager.beginTransaction()
        // transaction.replace(R.id.fragment_container, HomeFragment(this, username))
        transaction.replace(R.id.fragment_container, HomeFragment(this, username, id))

        transaction.addToBackStack(null)
        transaction.commit()
        connectButton.setOnClickListener {
            if (username.text.toString() == "") {
                val intentRegister = Intent(this, Login::class.java)
                intentRegister.apply {
                    startActivity(this)
                }

            }else{
                val intentRegister = Intent(this, Account::class.java)
                intentRegister.apply {
                    putExtra("username",username.text.toString())
                    startActivity(this)
                }

            }

        }

        // connection between menu_burger button and navigationView contained into drawerLayout
        val drawerLayout: DrawerLayout = findViewById(R.id.container_fragment)
        val menuIcon = findViewById<ImageView>(R.id.menu_burger)

        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle click on one item of NavigationView
        val navigationView = findViewById<NavigationView>(R.id.genres_navigation_view)

        // This variable will be used to get the title of the current item considering its id
        val menu = navigationView.menu

        // Here, we will basically add the event that consist of showing up the activity_main when
        // clicking on one item. Unfortunately we have to specify what to do for each item seperately
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_28 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_28)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "28"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)

                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_12 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_12)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    // Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "12"

                    Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_16 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_16)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "16"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_35 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_35)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "35"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_80 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_80)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "80"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_99 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_99)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "99"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_18 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_18)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "18"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_10751 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_10751)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "10751"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_14 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_14)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "14"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_36 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_36)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "36"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_27 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_27)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "27"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_10402 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_10402)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "10402"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_9648 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_9648)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "9648"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_10749 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_10749)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "10749"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_878 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_878)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "878"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_10770 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_10770)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "10770"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_53 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_53)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "53"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_10752 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_10752)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "10752"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                R.id.item_37 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_37)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    //Log.d("Succcess", "TITLE " + title.toString())

                    // We put the id as a number from item_id
                    val idGenre = "37"

                    // Log.d("Success", "IDDDDDDD" + idGenre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", idGenre)
                    intent.putExtra("current_genre", title)
                    intent.putExtra("username",username.text.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

                else -> {
                    // Do nothing
                }
            }
            true

        }


    }




}