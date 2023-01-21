package fi.eilco.android_project_gestion_film.fragments

import fi.eilco.android_project_gestion_film.MainActivity
import android.os.Bundle
import android.util.Log
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
import com.google.gson.GsonBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.adapter.GenreAdapter
import fi.eilco.android_project_gestion_film.models.GenreRoot

import kotlinx.coroutines.launch
import okhttp3.*

import java.io.IOException

class GenreFragment(private val context: MainActivity, private val username: TextView): Fragment() {

    private lateinit var s: SearchView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_genre, container, false)
        //recupérer le recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler_genre)
         s=view.findViewById(R.id.genre_search)

        lifecycleScope.launch {
            if (recycler != null) {
                getData(recycler)
            }
        }




        return view
    }

    private fun getData(recyclerView: RecyclerView) {

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/genre/movie/list?api_key=2174d146bb9c0eab47529b2e77d6b526&language=en-US")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val gson = GsonBuilder().create()
                val data = gson.fromJson(body, GenreRoot::class.java)

                //Set adapter and recycler view on UI with values get from http request
                activity?.runOnUiThread {
                    recyclerView.layoutManager= LinearLayoutManager(context)

                    val adapter = GenreAdapter(context, data.genres,this@GenreFragment)
                    recyclerView.adapter = adapter

                    //Function for getting search value
                    s.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }
                        //Filter recycler view values
                        override fun onQueryTextChange(newText: String?): Boolean {
                            adapter.filter.filter(newText)
                            return false
                        }

                    })

                }

            }

        })

    }
    //Go to home fragment
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