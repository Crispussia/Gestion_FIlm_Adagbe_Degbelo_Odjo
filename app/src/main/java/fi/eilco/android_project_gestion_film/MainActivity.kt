package fi.eilco.android_project_gestion_film

import android.annotation.SuppressLint
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


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectButton=findViewById<ImageView>(R.id.connect)
        //charger notre plant repository
        //injecter le fragment dans la boite (fragment_container)
        //val s=findViewById<SearchView>(R.id.movie_search)

        val username=findViewById<TextView>(R.id.textuser)
        val favorisButton=findViewById<ImageView>(R.id.favoris)
        val favorisButtonUser=findViewById<ImageView>(R.id.favoris_user_page)
        val textbadge=findViewById<TextView>(R.id.textbadge)
        val connectValidButton=findViewById<ImageView>(R.id.connectValide)
        var intentUser=intent.getStringExtra("username")
        val repo=FilmRepository()
        //textAnimals.text="$intentAnimals"
        Log.d("SucessIntent", intentUser.toString())

        if (intentUser.toString()!="null"){
            Log.d("SucessIntent", intentUser.toString())

            connectButton.visibility=View.GONE
            connectValidButton.setImageResource(R.drawable.ic_uservalide)
            connectValidButton.visibility=View.VISIBLE
            favorisButton.visibility = View.GONE;
            username.text="$intentUser"
            //mettre à jour la liste de plantes
            repo.updateFavorisImage(username,favorisButtonUser,textbadge)

        }else{
            connectValidButton.visibility=View.GONE
            favorisButtonUser.visibility=View.GONE
            connectButton.visibility=View.VISIBLE
            favorisButton.visibility = View.VISIBLE;
        }
        connectValidButton.setOnClickListener{
            val intentMain = Intent(this@MainActivity, Account::class.java)
            intentMain.apply {
                putExtra("username",username.text)
                startActivity(this)
            }
        }
        //Si on est sur la page d'accueil sans connection de l'utilisateur
        favorisButton.setOnClickListener{
            if(username.text.toString()==""){
                Toast.makeText(
                    this@MainActivity,
                    "Please connect to see your favorite list",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        favorisButtonUser.setOnClickListener{

            //var likedlist=repo.getUserLike(username)
            //Log.d("Je ne sais pas", likedlist.toString())
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, FavoriteFragment(this,username))
            transaction.addToBackStack(null)
            transaction.commit()

        }

        // Here we handle the case where a new activity_main has been launched when clicking on
        // one of the item of the navigationView
        val id = intent.getStringExtra("id")
        val currentGenre = intent.getStringExtra("current_genre")

        if (currentGenre != null){
            // We set the new current genre on the activity_main
            this.findViewById<TextView>(R.id.current_genre).text = currentGenre
        }


        val transaction = supportFragmentManager.beginTransaction()
       // transaction.replace(R.id.fragment_container, HomeFragment(this, username))
        transaction.replace(R.id.fragment_container, HomeFragment(this, username, id))

        transaction.addToBackStack(null)
        transaction.commit()
        connectButton.setOnClickListener{
            val intentRegister = Intent(this, Login::class.java)
            intentRegister.apply {
                startActivity(this)
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
        navigationView.setNavigationItemSelectedListener {
                 menuItem ->
            when (menuItem.itemId) {
                R.id.item_12 -> {
                    // Code to navigate to the main page
                    Log.d("SuccessIntent", "Item clicked")
                    // We get the title of the current id
                    val menuItem = menu.findItem(R.id.item_12)
                    val title = menuItem.title

                    // We set the new current_genre on activity_main screen
                    val currentGenre = this.findViewById<TextView>(R.id.current_genre)
                    currentGenre.text = title
                    Log.d("Succcess", "TITLE " + title.toString())

                    val currentGenre2 = this.findViewById<TextView>(R.id.current_genre)
                    Log.d("Succcess", "NEW GENRE " + currentGenre2.text.toString())

                    // We extract the id as a number from item_id
                    val string = "item_12"
                    val id_genre = string.substringAfter("_")

                    Log.d("Success", "IDDDDDDD" + id_genre)
                    // We start activity_main with the current genre
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", id_genre)
                    intent.putExtra("current_genre", title)
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