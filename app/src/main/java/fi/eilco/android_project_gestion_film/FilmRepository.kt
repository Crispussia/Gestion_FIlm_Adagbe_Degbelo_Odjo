package fi.eilco.android_project_gestion_film

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import fi.eilco.android_project_gestion_film.FilmRepository.Singleton.databaseRef
import fi.eilco.android_project_gestion_film.FilmRepository.Singleton.likedList
import fi.eilco.android_project_gestion_film.adapter.FavoriteAdapter
import fi.eilco.android_project_gestion_film.adapter.MovieAdapter
import fi.eilco.android_project_gestion_film.models.MovieModel
import fi.eilco.android_project_gestion_film.models.UserModel

class FilmRepository {
    //accéder à ces deux valeurs sur toutes l'application sans vider à chaque chargement
    object Singleton{
        //se connecter à la référence plante
        val databaseRef= FirebaseDatabase.getInstance().getReference("users")
        //creer une liste qui va contenir nos plantes
        var likedList= mutableListOf<Int>()
    }
    //demander les valeurs dans plantes et les injecter dans la list de plante avec une m"thode qui permettra d'actualiser le contenu
    fun updateFavorisImage(username:TextView,image:ImageView,badge:TextView){
        //retirer les anciennes plantes de la liste
        likedList.clear()
        //absorber les données depuis la databaseRef->liste des plantes
        databaseRef.child(username.text.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //recolter la liste
                var user=snapshot.getValue(UserModel::class.java)

                //construire un objet plante
                //likedList = snapshot.getValue(object : GenericTypeIndicator<UserModel>() {})!!
                if (user != null) {
                    likedList=user.liked
                    if (likedList.isNullOrEmpty()){
                        image.setImageResource(R.drawable.ic_unlike)
                        image.visibility= View.VISIBLE
                        badge.visibility=View.GONE
                    }
                   else{
                        image.setImageResource(R.drawable.ic_like)
                        image.visibility= View.VISIBLE
                        badge.text= likedList.size.toString()
                        badge.visibility=View.VISIBLE
                   }
                    Log.d("Film Repository", likedList.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun getUserLike(username:TextView):MutableList<Int>{
        likedList.clear()
        //absorber les données depuis la databaseRef->liste des plantes
        databaseRef.child(username.text.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //recolter la liste
                var user=snapshot.getValue(UserModel::class.java)

                //construire un objet plante
                //likedList = snapshot.getValue(object : GenericTypeIndicator<UserModel>() {})!!
                if (user != null) {

                    likedList=user.liked
                    Log.d("Film Repository", likedList.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return likedList
    }
    //demander les valeurs dans plantes et les injecter dans la list de plante avec une m"thode qui permettra d'actualiser le contenu
    fun updateData(callback: ()->Unit,username: TextView){
        //retirer les anciennes plantes de la liste
        likedList.clear()
        //absorber les données depuis la databaseRef->liste des plantes
        databaseRef.child(username.text.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user=snapshot.getValue(UserModel::class.java)

                //construire un objet plante
                //likedList = snapshot.getValue(object : GenericTypeIndicator<UserModel>() {})!!
                if (user != null) {

                    likedList=user.liked
                    Log.d("Film Repository", likedList.toString())
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}