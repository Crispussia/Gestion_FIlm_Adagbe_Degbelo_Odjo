package fi.eilco.android_project_gestion_film.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.*
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.fragments.DetailFragment
import fi.eilco.android_project_gestion_film.fragments.GenreFragment
import fi.eilco.android_project_gestion_film.fragments.HomeFragment
import fi.eilco.android_project_gestion_film.models.GenreModel
import fi.eilco.android_project_gestion_film.models.MovieModel
import fi.eilco.android_project_gestion_film.models.UserModel
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter(private val context: MainActivity, private val movieList:List<MovieModel>, private val username: TextView, private val movieContext: HomeFragment): RecyclerView.Adapter<MovieAdapter.ViewHolder> (),
    Filterable {

    var movieFilterList = movieList

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val movieImage=view.findViewById<ImageView>(R.id.image_item)
        val movieName=view.findViewById<TextView>(R.id.item_movie_title)
        val movieRate=view.findViewById<TextView>(R.id.item_movie_rate)
        val movieVotes=view.findViewById<TextView>(R.id.item_movie_votes)
        val likeIcon=view.findViewById<ImageView>(R.id.ic_heart_empty)
        val card=view.findViewById<CardView>(R.id.main_card_view)


    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_film,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Récupérer les information d'un film
        val currentMovie=movieFilterList[position]
        Log.d("hhjjjjjjjjjjjj", "flitttttreeeeeeeeee22bb" +movieList)


        //utiliser glide pour récupérer l'image du film à partir de son lien
        Glide.with(context).load(Uri.parse("https://image.tmdb.org/t/p/original/"+currentMovie.poster_path)).into(holder.movieImage)

        holder.movieName.text=currentMovie.original_title
        holder.movieRate.text="Rate : "+currentMovie.vote_average
        holder.movieVotes.text="Votes : "+currentMovie.vote_count

        holder.card.setOnClickListener{

            movieContext.onClick(currentMovie.id,currentMovie.original_title)
        }






        val databaseRef= FirebaseDatabase.getInstance().getReference("users").child(username.text.toString())
        //On récupère les éléments likes de la BD pour un utilisateur connecté

        if (username.text.toString()!="") {

            //creer une liste qui va contenir nos plantes
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Log.d("Sucess", snapshot.value.toString())
                    var user=snapshot.getValue(UserModel::class.java)
                    Log.d("SucesséSDFGCVB", snapshot.value.toString())
                    if (user != null) {
                        //construire un objet plante
                        val list = user.liked
                        list?.forEach { element ->
                            Log.d("ELEMENT", element.toString())
                        }
                        if (list.isNullOrEmpty()) {

                            holder.likeIcon.setImageResource(R.drawable.ic_heart_empty)
                        }
                        else{
                            Log.d("bon=============","ssDFGHJs")
                            if (list.contains(currentMovie.id)) {
                                Log.d("bon=============","sssssss")
                                holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
                            }else{
                                holder.likeIcon.setImageResource(R.drawable.ic_heart_empty)
                            }
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        //rajouter une interaction sur ce coeur
        holder.likeIcon.setOnClickListener{
            //On vérifie la présence d'un utilisateur avant d'ajouter des likes
            if (username.text.toString()!="") {
                Log.d("Bon je suis fatiguée", "Bon je suis fatiguée")
                /*var likedlist= FilmRepository.Singleton.likedList
                Log.d("Bon je suis fatiguée", likedlist.toString())
                likedlist?.forEach { element ->
                    Log.d("ELEMENT", element.toString())
                }
                //On vérifie bien que la liste des éléments likés n'est pas vide ou null
                if (likedlist.isNullOrEmpty()) {
                    likedlist= mutableListOf(currentMovie.id)
                    //On met à jour l'icon à like
                    holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
                    Log.d("Ajout d'un nouvelle", likedlist.toString())
                }else {
                    //Si la liste contient l'élement liké
                    if (likedlist.contains(currentMovie.id)) {
                        //On le supprime de la liste des éléments likés
                        likedlist.remove(currentMovie.id)
                        //On met à jour l'icone
                        holder.likeIcon.setImageResource(R.drawable.ic_heart_empty)
                        //On vérifie que la liste retourner après suppression n'est pas vide
                        if (likedlist.isNotEmpty()) {
                            Log.d("Suppression", likedlist.toString())
                            //On met à jour l'utilisateur
                            updateUser(username,likedlist)
                            // code à exécuter si la liste contient au moins un élément
                        } else {
                            Log.d("el supprimer de current", likedlist.toString())
                            updateUser(username,likedlist)
                        }

                    }else{
                        //On ajoute à la liste quand l'élément existe
                        likedlist.add(currentMovie.id)
                        Log.d("Ajout", likedlist.toString())
                        //On met à jour l'icône
                        holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
                        //On met à jour la Bd
                        updateUser(username,likedlist)
                    }
                }*/
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
                                likedlist= mutableListOf(currentMovie.id)
                                //On met à jour l'icon à like
                                holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
                                //On met à jour la liste
                                updateUser(username,likedlist)
                                Log.d("Ajout d'un nouvelle", likedlist.toString())

                            } else {
                                //Si la liste contient l'élement liké
                                if (likedlist.contains(currentMovie.id)) {
                                    //On le supprime de la liste des éléments likés
                                    likedlist.remove(currentMovie.id)
                                    //On met à jour l'icone
                                    holder.likeIcon.setImageResource(R.drawable.ic_heart_empty)
                                    //On vérifie que la liste retourner après suppression n'est pas vide
                                    if (likedlist.isNotEmpty()) {
                                        Log.d("Suppression", likedlist.toString())
                                        //On met à jour l'utilisateur
                                        updateUser(username,likedlist)
                                        // code à exécuter si la liste contient au moins un élément
                                    } else {
                                        Log.d("el supprimer de current", likedlist.toString())
                                        updateUser(username,likedlist)
                                    }

                                }else{
                                    //On ajoute à la liste quand l'élément existe
                                    likedlist.add(currentMovie.id)
                                    Log.d("Ajout", likedlist.toString())
                                    //On met à jour l'icône
                                    holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
                                    //On met à jour la Bd
                                    updateUser(username,likedlist)
                                }
                            }
                        }

                        databaseRef.removeEventListener(this)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }else{
                Toast.makeText(context,
                    "Please connect toi pour ajouter",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    override fun getItemCount(): Int {
        return movieFilterList.size //compte le nombre de film
    }




        //On met à jour l'utilisateur ainsi que la nouvelle liste
    fun updateUser(username: TextView,liked:MutableList<Int>){
        val databaseRef= FirebaseDatabase.getInstance().getReference("users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                // Log.d("Sucess", snapshot.value.toString())
                //On vérifie que l'username existe parmi les enfants du snapshot
                if (snapshot.hasChild(username.text.toString())) {
                    //On récupère la liste des le usermodèle de l'utilisateur
                    var user=snapshot.child(username.text.toString()).getValue(object : GenericTypeIndicator<UserModel>() {})!!
                    user.liked=liked
                    //On met à jour le snasphot de l'utilisateur
                    databaseRef.child(username.text.toString()).setValue(user)
                    databaseRef.removeEventListener(this)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.d("hhjjjjjjjjjjjj", "flitttttreeeeeeeeee" +"")
                //Log.d("hhjjjjjjjjjjjj", "flitttttreeeeeeeeee" +movieFilterList)


                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    movieFilterList = movieList
                } else {
                    val resultList = ArrayList<MovieModel>()
                    for (row in movieList) {
                        if (row.original_title.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    movieFilterList = resultList

                }
                val filterResults = FilterResults()
                filterResults.values = movieFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieFilterList = results?.values as ArrayList<MovieModel>
                notifyDataSetChanged()
            }

        }
    }

}