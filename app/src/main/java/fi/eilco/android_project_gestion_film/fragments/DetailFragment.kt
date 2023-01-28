package fi.eilco.android_project_gestion_film.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.models.MovieModel
import fi.eilco.android_project_gestion_film.models.UserModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class DetailFragment (private val context: MainActivity, private val username: TextView): Fragment() {

    private lateinit var movieImage: ImageView
    private lateinit var overview: TextView
    private lateinit var title: TextView
    private lateinit var like: ImageView

    private lateinit var popularity:TextView
    private lateinit var realeseDate:TextView
    private lateinit var rate:TextView
    private lateinit var votes:TextView




    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_detail, container, false)
        //recupérer le recycler view
        if (view != null) {
            movieImage= view.findViewById(R.id.image_detail)
            overview=view.findViewById(R.id.overwiew)
            title=view.findViewById(R.id.item_detail_title)
            popularity=view.findViewById(R.id.popularity)
            rate=view.findViewById(R.id.item_detail_rate)
            realeseDate=view.findViewById(R.id.release_date)
            votes=view.findViewById(R.id.item_detail_votes)

            like=view.findViewById(R.id.ic_heart_empty)
        }
        lifecycleScope.launch {
                getData()

        }

        return view
    }

    private fun getData() {

        var genreId: Int = 0
        var genreName: String = ""

        setFragmentResultListener("detailID") { key, bundle ->
            genreId = bundle.getInt("genre_id")

            setFragmentResultListener("detailName") { key, bundle ->
                genreName = bundle.getString("genre_name").toString()

                Log.d("hhjjjjjjjjjjjj", "44444444444444444444444445555 " + genreId.toString())


                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+genreId+"?api_key=2174d146bb9c0eab47529b2e77d6b526&language=en-US" )
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
                        activity?.runOnUiThread {
                            title.text=data.original_title
                            popularity.text=popularity.text.toString() + data.popularity
                            realeseDate.text=realeseDate.text.toString()+data.release_date
                            rate.text=rate.text.toString()+data.vote_average
                            votes.text= votes.text.toString()+data.vote_count
                            Glide.with(context).load(Uri.parse("https://image.tmdb.org/t/p/original/"+data.poster_path)).into(movieImage)
                            overview.text=overview.text.toString() + "\n"+data.overview
                            if (username.text.toString()!=""){

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
                            }

                        }

                    }

                })


           }
        }
    }
    fun onClick( genreID: Int,genreName: String) {
        setFragmentResult("secret", bundleOf("genre_id" to genreID))
        setFragmentResult("secret2", bundleOf("genre_name" to genreName))


        val fragmentTransaction=this.parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container,HomeFragment(context,username))
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}