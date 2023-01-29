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
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.fragments.FavoriteFragment
import fi.eilco.android_project_gestion_film.fragments.GenreFragment
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
        val transaction = supportFragmentManager.beginTransaction()
       // transaction.replace(R.id.fragment_container, HomeFragment(this, username))
        transaction.replace(R.id.fragment_container, GenreFragment(this, username))

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
    }




}