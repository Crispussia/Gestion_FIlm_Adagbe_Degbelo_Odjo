package fi.eilco.android_project_gestion_film

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fi.eilco.android_project_gestion_film.models.UserModel

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val username=findViewById<TextView>(R.id.username_valeur)
        val nombre_favoris=findViewById<TextView>(R.id.favoris_count_val)
        val favorisButton=findViewById<ImageView>(R.id.ic_heart_empty)
        val deconnectButton=findViewById<AppCompatButton>(R.id.deconnection)
        val intentUser=intent.getStringExtra("username")

        username.text="$intentUser"
        Log.d("User",username.text.toString())
        var likedlist= FilmRepository.Singleton.likedList

        Log.d("Marche",likedlist.toString())
        nombre_favoris.text=likedlist.size.toString()
        if (likedlist.size==0){
            favorisButton.setImageResource(R.drawable.ic_unlike)
        }else{
            favorisButton.setImageResource(R.drawable.ic_like)
        }
        favorisButton.setOnClickListener{
            if (likedlist.size==0){
                Toast.makeText(
                    this@Account,
                    "Vous n'avez aucun favoris",
                    Toast.LENGTH_SHORT
                ).show()
            }else{

            }
        }
        deconnectButton.setOnClickListener{
            val intentMain = Intent(this@Account, MainActivity::class.java)
            intentMain.apply {
                putExtra("username","null")
                startActivity(this)
            }
        }

    }
}