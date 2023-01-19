package fi.eilco.android_project_gestion_film.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.FilmRepository
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.models.UserModel

class FavoriteAdapter (
    private  val context: MainActivity,
    private val likedList : MutableList<Int>,private val username: TextView): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

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

        //val textUser=view.findViewById<TextView>(R.id.textuser)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_card_film,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //elle s'actionne pour chacune des identifiants des films likés
        val currentFilm =likedList[position]

        holder.movieName.text=currentFilm.toString()
        holder.likeIcon.setImageResource(R.drawable.ic_like)
        //rajouter une interaction sur cette étoile
        holder.likeIcon.setOnClickListener{
            //var likedlist=repo.getUserLike(username)
            likedList.remove(currentFilm)
            Log.d("Current Film",currentFilm.toString())
            Log.d("Current Film",likedList.toString())
            notifyItemRemoved(position)
            updateLikeList(username,likedList)


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



}