package fi.eilco.android_project_gestion_film.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import fi.eilco.android_project_gestion_film.MainActivity
import fi.eilco.android_project_gestion_film.R
import fi.eilco.android_project_gestion_film.models.MovieModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class DetailFragment (private val context: MainActivity, private val username: TextView): Fragment() {

    private lateinit var movieImage: ImageView
    private lateinit var overview: TextView
    private lateinit var title: TextView




    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_detail, container, false)
        //recupérer le recycler view
        if (view != null) {
            movieImage= view.findViewById(R.id.image_detail)
            overview=view.findViewById(R.id.overwiew)
            title=view.findViewById(R.id.item_detail_title)
        }
        lifecycleScope.launch {
                getData()

        }




        return view
    }

    private fun getData() {

        var genreId: Int = 0
        var genreName: String = ""

        setFragmentResultListener("detailID") { key, bundle ->
            genreId = bundle.getInt("genre_id")

            setFragmentResultListener("detailName") { key, bundle ->
                genreName = bundle.getString("genre_name").toString()

                Log.d("hhjjjjjjjjjjjj", "44444444444444444444444445555 " + genreId.toString())


                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+genreId+"?api_key=2174d146bb9c0eab47529b2e77d6b526&language=en-US" )
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

                        //Set adapter and recycler view on UI with values get from http request
                        activity?.runOnUiThread {
                            title.text=data.original_title
                            Glide.with(context).load(Uri.parse("https://image.tmdb.org/t/p/original/"+data.poster_path)).into(movieImage)
                            overview.text=overview.text.toString() + "\n"+data.overview

                        }

                    }

                })


           }
        }
    }
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