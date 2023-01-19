package fi.eilco.android_project_gestion_film.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.adapter.MovieAdapter
import fi.eilco.android_project_gestion_film.models.RootModel

import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class HomeFragment(private val context: MainActivity, private val username: TextView): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)
        //recupérer le recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        Log.d("recycler : ","tttttttttttttttttttttttttttttt")

        lifecycleScope.launch {
            if (recycler != null) {
                getData(recycler)
            }
        }

        Log.d("recycler : ","lllllllllllllllllllllllllll")



        return view
    }

    private fun getData(recyclerView: RecyclerView) {

        var genreId: Int = 0
        var genreName: String = ""
        setFragmentResultListener("secret") { key, bundle ->
            genreId = bundle.getInt("genre_id")

            setFragmentResultListener("secret2") { key, bundle ->
                genreName = bundle.getString("genre_name").toString()

                Log.d("hhjjjjjjjjjjjj", "eeeeeeeemmmmmmmmmmmmmmmm " + genreId.toString())
                Log.d("hhjjjjjjjjjjjj", "eeeeeeeemmmmmmmmmmmmmmmm " + genreName)


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
                        Log.d("hhjjjjjjjjjjjj", "eeeeeeeeeeeeeeeeeeeeeee")

                        //Set adapter and recycler view on UI with values get from http request
                        activity?.runOnUiThread {
                            recyclerView.layoutManager = LinearLayoutManager(context)

                            val adapter = MovieAdapter(context, data.results,username)
                            recyclerView.adapter = adapter
                        }

                    }

                })


            }
        }
    }
}