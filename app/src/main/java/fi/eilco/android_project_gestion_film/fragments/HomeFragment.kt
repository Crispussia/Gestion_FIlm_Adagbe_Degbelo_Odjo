package fi.eilco.android_project_gestion_film.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.DescriptionActivity
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.adapter.ItemCardFilmDecoration
import fi.eilco.android_project_gestion_film.adapter.MovieAdapter
import fi.eilco.android_project_gestion_film.models.RootModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException


class HomeFragment(private val context: MainActivity, private val username: TextView, private val id_genre: String?): Fragment() {
    private lateinit var s: SearchView
    private lateinit var genre: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)
        //val view2 = inflater?.inflate(R.layout.activity_main, container, false)
        if (view != null) {
            genre=  view.findViewById(R.id.current_genre1)
        }

        //recupérer le recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        recycler?.addItemDecoration(ItemCardFilmDecoration())





        lifecycleScope.launch {
            if (recycler != null) {
                getData(recycler)
            }
        }


        //s= context.findViewById(R.id.movie_search)


        return view
    }

    private fun getData(recyclerView: RecyclerView) {

        var genreId: Int = -1

        // With that condition we can movies of the genre selected on the navigationView
        if (this.id_genre != null){
            genreId = this.id_genre.toInt()
            Log.d("Success", "GENRE_IDDDDDD " + genreId.toString())
        }

        var genreName: String = ""
        //s= context.findViewById(R.id.movie_search)



        if (genreId==-1){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?api_key=2174d146bb9c0eab47529b2e77d6b526&with_genres=28")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    e.printStackTrace()
                }


                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    println(body)
                    val gson = GsonBuilder().create()
                    val data = gson.fromJson(body, RootModel::class.java)

                    //Set adapter and recycler view on UI with values get from http request
                    activity?.runOnUiThread {
                        genre.text=genreName
                        recyclerView.layoutManager = LinearLayoutManager(context)

                        val adapter = MovieAdapter(context, data.results,username,this@HomeFragment)
                        recyclerView.adapter = adapter

                    }

                }

            })

        }

        Log.d("hhjjjjjjjjjjjj", "eeeeeeeeeehommmmmehhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+genreId.toString())
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/discover/movie?api_key=2174d146bb9c0eab47529b2e77d6b526&with_genres=" + genreId.toString())
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                e.printStackTrace()
            }


            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val gson = GsonBuilder().create()
                val data = gson.fromJson(body, RootModel::class.java)
                Log.d("hhjjjjjjjjjjjj", "eeeeeeeeeehommmmme" +
                        "")

                //Set adapter and recycler view on UI with values get from http request
                activity?.runOnUiThread {
                    recyclerView.layoutManager = LinearLayoutManager(context)

                    val adapter = MovieAdapter(context, data.results,username,this@HomeFragment)
                    recyclerView.adapter = adapter
                    /*
                    //Function for getting search value
                    s.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }
                        //Filter recycler view values
                        override fun onQueryTextChange(newText: String?): Boolean {
                            adapter.filter.filter(newText)
                            return false
                        }

                    })*/
                }

            }

        })


        setFragmentResultListener("secret") { key, bundle ->
            genreId = bundle.getInt("genre_id")

            setFragmentResultListener("secret2") { key, bundle ->
                genreName = bundle.getString("genre_name").toString()

                Log.d("hhjjjjjjjjjjjj", "NEW_GEEEEEE"+genreId.toString())


            }
        }
    }
    fun onClick( movieID: Int,movieName: String) {

        setFragmentResult("detailID", bundleOf("genre_id" to movieID))
        setFragmentResult("detailName", bundleOf("genre_name" to movieName))


        /*val fragmentTransaction=this.parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container,DetailFragment(context,username))
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()*/
        val intent = Intent(context, DescriptionActivity::class.java)
        intent.putExtra("detailID", movieID)
        startActivity(intent)

    }
}