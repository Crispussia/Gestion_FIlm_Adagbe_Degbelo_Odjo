package fi.eilco.android_project_gestion_film.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.FavoriteActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.models.MovieModel
import fi.eilco.android_project_gestion_film.models.UserModel
import fi.eilco.android_project_gestion_film.fragments.FavoriteFragment
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter(
    private val context: FavoriteActivity,
    private val favoriteContext: FavoriteFragment,
    private val likedList: MutableList<Int>, private val username: TextView, private val movieList:List<MovieModel>
): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>()/*,
    Filterable*/ {
    var favoriteFilterList = movieList

    fun removeAt(position: Int) {
        likedList.removeAt(position)
        notifyItemRemoved(position)

    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val movieImage=view.findViewById<ImageView>(R.id.image_item)
        val movieName=view.findViewById<TextView>(R.id.item_movie_title)
        val movieRate=view.findViewById<TextView>(R.id.item_movie_rate)
        val movieVotes=view.findViewById<TextView>(R.id.item_movie_votes)
        val likeIcon=view.findViewById<ImageView>(R.id.ic_heart_empty)
        val card=view.findViewById<CardView>(R.id.main_card_view)

        //val textUser=view.findViewById<TextView>(R.id.textuser)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_card_film,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentFilm =movieList[position]
        Log.d("Length",movieList.size.toString())
        Glide.with(context).load(Uri.parse("https://image.tmdb.org/t/p/original/"+currentFilm.poster_path)).into(holder.movieImage)
        holder.movieName.text=currentFilm.original_title
        holder.movieRate.text=holder.movieRate.text.toString()+" "+currentFilm.vote_average
        holder.movieVotes.text=holder.movieVotes.text.toString()+" "+currentFilm.vote_count
        holder.likeIcon.setImageResource(R.drawable.ic_heart_filled_red)
        //rajouter une interaction sur cette étoile
        holder.likeIcon.setOnClickListener{
            //var likedlist=repo.getUserLike(username)
            likedList.remove(likedList[position])
            Log.d("Current Film",currentFilm.toString())
            Log.d("Current Film",likedList.toString())
            notifyItemRemoved(position)
            updateLikeList(username,likedList)
        }

        holder.card.setOnClickListener{
            favoriteContext.onClick(currentFilm.id,currentFilm.original_title)
        }

    }

    override fun getItemCount(): Int =likedList.size

    fun updateLikeList(username: TextView,liked:MutableList<Int>){
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


    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.d("hhjjjjjjjjjjjj", "flitttttreeeeeeeeee" +"")
                //Log.d("hhjjjjjjjjjjjj", "flitttttreeeeeeeeee" +movieFilterList)


                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    favoriteFilterList = movieList
                } else {
                    val resultList = ArrayList<MovieModel>()
                    for (row in movieList) {
                        if (row.original_title.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    favoriteFilterList = resultList

                }
                val filterResults = FilterResults()
                filterResults.values = favoriteFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                favoriteFilterList = results?.values as ArrayList<MovieModel>
                notifyDataSetChanged()
            }

        }
    }*/

}