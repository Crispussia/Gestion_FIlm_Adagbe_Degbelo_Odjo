package fi.eilco.android_project_gestion_film.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.fragments.GenreFragment
import fi.eilco.android_project_gestion_film.models.GenreModel

class GenreAdapter (private val context: MainActivity, private  val genreList:List<GenreModel>, private val genreContext: GenreFragment): RecyclerView.Adapter<GenreAdapter.ViewHolder> (){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val genreName=view.findViewById<TextView>(R.id.genre_name)


    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_genre,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Récupérer les information d'un film
        val currentGenre=genreList[position]

        holder.genreName.text=currentGenre.name
        holder.genreName.setOnClickListener{
            genreContext.onClick(currentGenre.id,currentGenre.name)
        }

    }

    override fun getItemCount(): Int {
        return genreList.size //compte le nombre de film
    }

}