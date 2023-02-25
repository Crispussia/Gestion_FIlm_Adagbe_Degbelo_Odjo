package fi.eilco.android_project_gestion_film

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fi.eilco.android_project_gestion_film.fragments.FavoriteFragment

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        var intentUser = intent.getStringExtra("username")
        val connectButton = findViewById<ImageView>(R.id.connect)
        val favorisButton = findViewById<ImageView>(R.id.favoris)
        //val favorisButtonUser = findViewById<ImageView>(R.id.favoris_user_page)
        val textbadge = findViewById<TextView>(R.id.textbadge)
        val username = findViewById<TextView>(R.id.textuser)
        var likedlist= FilmRepository.Singleton.likedList
        username.text = "$intentUser"
        val repo = FilmRepository()
        if (intentUser.toString() != "null") {
            favorisButton.setImageResource(R.drawable.ic_heart_filled_red);
        }
        Log.d("Nombre d'élément", likedlist.size.toString())

        textbadge.text= FilmRepository.Singleton.likedList.size.toString()
        textbadge.visibility= View.VISIBLE
        /*favorisButton.setOnClickListener{
            textbadge.text = (FilmRepository.Singleton.likedList.size - 1).toString()
        }*/
        Log.d("SucessIntent", intentUser.toString())
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_favorite_container, FavoriteFragment(this@FavoriteActivity, username,textbadge))
        transaction.addToBackStack(null)
        transaction.commit()
        connectButton.setOnClickListener {

            val intentRegister = Intent(this, Account::class.java)
            intentRegister.apply {
                putExtra("username",username.text.toString())
                startActivity(this)
            }

        }


        // Go back to the previous activity when clicking on back_arrow
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener{
            finish()
        }
    }
}