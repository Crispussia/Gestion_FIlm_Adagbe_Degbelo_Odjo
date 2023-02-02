package fi.eilco.android_project_gestion_film.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.FavoriteActivity
import fi.eilco.android_project_gestion_film.FilmRepository.Singleton.likedList
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.adapter.FavoriteAdapter
import fi.eilco.android_project_gestion_film.adapter.ItemCardFilmDecoration
import fi.eilco.android_project_gestion_film.models.MovieModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class FavoriteFragment(
    private val context: FavoriteActivity, private val username: TextView
): Fragment() {
    private lateinit var s: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_collection, container, false)
        //recupérer le recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        recycler?.addItemDecoration(ItemCardFilmDecoration())
        /* if (view != null) {
            s=view.findViewById(R.id.favorite_search)
        }*/

        //recycler?.adapter=FavoriteAdapter(context,likedList,username)
        recycler?.layoutManager= LinearLayoutManager(context)

        lifecycleScope.launch {
            if (recycler != null) {
                getData(recycler)
            }
        }
        return view

    }

    private fun getData(recyclerView: RecyclerView) {
        val client = OkHttpClient()
        val movieList= ArrayList<MovieModel>()
        for (i in 1 .. likedList.size){
            val movieId= likedList[i-1]

            val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/"+movieId+"?api_key=2174d146bb9c0eab47529b2e77d6b526&language=en-US")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
            }


            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val gson = GsonBuilder().create()
                val data = gson.fromJson(body, MovieModel::class.java)
                movieList+=data
                //Set adapter and recycler view on UI with values get from http request
                activity?.runOnUiThread {
                    recyclerView.layoutManager= LinearLayoutManager(context)

                    val adapter = FavoriteAdapter(context,likedList,username,movieList)

                    recyclerView.adapter = adapter

                    //Function for getting search value
                   /* s.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }
                        //Filter recycler view values
                        override fun onQueryTextChange(newText: String?): Boolean {
                            //adapter.filter.filter(newText)
                            return false
                        }

                    })*/

                }

            }

        })}

    }

    //A revoir
   /* fun onClick( movieID: Int,movieName: String) {

        setFragmentResult("detailID", bundleOf("genre_id" to movieID))
        setFragmentResult("detailName", bundleOf("genre_name" to movieName))


        val fragmentTransaction=this.parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container,DetailFragment(context,username))
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }*/

}