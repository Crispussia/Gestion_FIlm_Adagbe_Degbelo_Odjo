package fi.eilco.android_project_gestion_film.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.fragments.GenreFragment
import fi.eilco.android_project_gestion_film.models.GenreModel
import java.util.*
import kotlin.collections.ArrayList

class GenreAdapter (private val context: MainActivity, private  val genreList:List<GenreModel>, private val genreContext: GenreFragment): RecyclerView.Adapter<GenreAdapter.ViewHolder> (),
    Filterable {
    var genreFilterList = genreList


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val genreName = view.findViewById<TextView>(R.id.genre_name)
        val genreTitle=view.findViewById<TextView>(R.id.current_genre)


    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_genre, parent, false)
        genreFilterList = genreList
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Récupérer les information d'un film
        val currentGenre = genreFilterList[position]

        holder.genreName.text = currentGenre.name
        holder.genreName.setOnClickListener {
            genreContext.onClick(currentGenre.id, currentGenre.name)
        }

    }

    override fun getItemCount(): Int {
        return genreFilterList.size //compte le nombre de film
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    genreFilterList = genreList
                } else {
                    val resultList = ArrayList<GenreModel>()
                    for (row in genreList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    genreFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = genreFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                genreFilterList = results?.values as ArrayList<GenreModel>
                notifyDataSetChanged()            }

        }
    }
}