package fi.eilco.android_project_gestion_film

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.models.MovieModel
import fi.eilco.android_project_gestion_film.models.UserModel
import fi.eilco.android_project_gestion_film.user_data.SharedPrefHelper
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class DescriptionActivity : AppCompatActivity() {
    private lateinit var movieImage: ImageView
    private lateinit var overview: TextView
    private lateinit var title: TextView
    private lateinit var like: ImageView

    private lateinit var popularity: TextView
    private lateinit var realeseDate: TextView
    private lateinit var rate: TextView
    private lateinit var votes: TextView

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val detailID = intent.getIntExtra("detailID",22)
        Log.d("hhjjjjjjjjjjjj", "eeeeeeeeeehommmmmehhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+detailID.toString())

        /* Beginning of Testing SharedPrerences for session variables */
        sharedPrefHelper = SharedPrefHelper(this)
        val sessionVariable = sharedPrefHelper.getString("session_variable")// -1 is returned when the session variable is not found
        val sessionVariable2 = sharedPrefHelper.getString("session_var")// -1 is returned when the session variable is not found

        Log.d("Session Variable testing", "DESCRIPTION_SESSIONNNNNNNNN " + sessionVariable.toString())
        Log.d("Session Variable testing", "DESCRIPTION_SESSIONNNNNNNNN " + sessionVariable2.toString())
        /* End of Testing SharedPrerences for session variables */


        movieImage= findViewById(R.id.image_item)
        overview=findViewById(R.id.movie_overview)
        title=findViewById(R.id.title_movie_description)
        popularity=findViewById(R.id.movie_popularity)
        rate=findViewById(R.id.movie_vote_average)
        realeseDate=findViewById(R.id.movie_release_date)
        votes=findViewById(R.id.movie_vote_count)

        //like=findViewById(R.id.ic_heart_empty)

        if (detailID != null) {
            getData(detailID)
        }

        // Go back to the previous activity when clicking on back_arrow
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener{
            finish()
        }
    }


    private fun getData(detailID:Int) {



                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+detailID+"?api_key=2174d146bb9c0eab47529b2e77d6b526&language=en-US" )
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        e.printStackTrace()
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        println(body)
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(body, MovieModel::class.java)

                        //Set adapter and recycler view on UI with values get from http request
                        runOnUiThread {
                            title.text = data.original_title
                            popularity.text = popularity.text.toString() + "" +  data.popularity
                            realeseDate.text = realeseDate.text.toString() + "" + data.release_date
                            rate.text = rate.text.toString() + "" + data.vote_average
                            votes.text = votes.text.toString() + "" + data.vote_count
                            Glide.with(this@DescriptionActivity).load(Uri.parse("https://image.tmdb.org/t/p/original/"+data.poster_path)).into(movieImage)
                            overview.text = overview.text.toString() + "\n" + data.overview
                            /* if (username.text.toString()!=""){

                                //On récupère la référence de la liste des éléments likés par l'utilisateur
                                val databaseRef= FirebaseDatabase.getInstance().getReference("users").child(username.text.toString())

                                databaseRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot){
                                        //On récupère le User sous forme de Usermodel
                                        var user=snapshot.getValue(UserModel::class.java)
                                        Log.d("SucesséSDFGCVB", snapshot.value.toString())
                                        if (user != null) {
                                            //On récupère la liste des éléments likés
                                            var likedlist = user.liked
                                            likedlist?.forEach { element ->
                                                Log.d("ELEMENT", element.toString())
                                            }
                                            if (likedlist.contains(data.id)) {
                                                like.setImageResource(R.drawable.ic_like)
                                            }else{
                                                like.setImageResource(R.drawable.ic_unlike)
                                            }

                                        }
                                        databaseRef.removeEventListener(this)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                            }
                            like.setOnClickListener{
                                if (username.text.toString()!="") {
                                    //On récupère la référence de la liste des éléments likés par l'utilisateur
                                    val databaseRef= FirebaseDatabase.getInstance().getReference("users").child(username.text.toString())

                                    databaseRef.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot){
                                            //On récupère le User sous forme de Usermodel
                                            var user=snapshot.getValue(UserModel::class.java)
                                            Log.d("SucesséSDFGCVB", snapshot.value.toString())
                                            if (user != null) {
                                                //On récupère la liste des éléments likés
                                                var likedlist = user.liked
                                                likedlist?.forEach { element ->
                                                    Log.d("ELEMENT", element.toString())
                                                }
                                                //On vérifie bien que la liste des éléments likés n'est pas vide ou null
                                                if (likedlist.isNullOrEmpty()) {

                                                    //Si la liste est nulle on crée une mutable liste qui contient la liste des élements likés
                                                    likedlist= mutableListOf(data.id)
                                                    //On met à jour l'icon à like
                                                    like.setImageResource(R.drawable.ic_like)
                                                    //On met à jour la liste
                                                    user.liked=likedlist
                                                    //On met à jour le snasphot de l'utilisateur
                                                    databaseRef.setValue(user)

                                                    Log.d("Ajout d'un nouvelle", likedlist.toString())

                                                } else {
                                                    //Si la liste contient l'élement liké
                                                    if (likedlist.contains(data.id)) {
                                                        //On le supprime de la liste des éléments likés
                                                        likedlist.remove(data.id)
                                                        //On met à jour l'icone
                                                        like.setImageResource(R.drawable.ic_unlike)
                                                        //On vérifie que la liste retourner après suppression n'est pas vide
                                                        if (likedlist.isNotEmpty()) {
                                                            Log.d("Suppression", likedlist.toString())
                                                            //On met à jour l'utilisateur
                                                            //On met à jour la liste
                                                            user.liked=likedlist
                                                            //On met à jour le snasphot de l'utilisateur
                                                            databaseRef.setValue(user)
                                                            // code à exécuter si la liste contient au moins un élément
                                                        } else {
                                                            Log.d("el supprimer de current", likedlist.toString())
                                                            //On met à jour la liste
                                                            user.liked=likedlist
                                                            //On met à jour le snasphot de l'utilisateur
                                                            databaseRef.setValue(user)
                                                        }

                                                    }else{
                                                        //On ajoute à la liste quand l'élément existe
                                                        likedlist.add(data.id)
                                                        Log.d("Ajout", likedlist.toString())
                                                        //On met à jour l'icône
                                                        like.setImageResource(R.drawable.ic_like)
                                                        //On met à jour la Bd
                                                        //On met à jour la liste
                                                        user.liked=likedlist
                                                        //On met à jour le snasphot de l'utilisateur
                                                        databaseRef.setValue(user)
                                                    }
                                                }
                                            }

                                            databaseRef.removeEventListener(this)

                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                }
                                else{
                                    Toast.makeText(context,
                                        "Please connect toi pour ajouter",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }*/


                        }
                    }

                })



    }
}