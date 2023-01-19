package fi.eilco.android_project_gestion_film.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fi.eilco.android_project_gestion_film.FilmRepository.Singleton.likedList
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.adapter.FavoriteAdapter

class FavoriteFragment (
    private  val context: MainActivity,private val username: TextView): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_collection, container, false)
        //recupérer le recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.collection_recycler_list)
        recycler?.adapter=FavoriteAdapter(context,likedList,username)
        recycler?.layoutManager= LinearLayoutManager(context)

        return view

    }
}