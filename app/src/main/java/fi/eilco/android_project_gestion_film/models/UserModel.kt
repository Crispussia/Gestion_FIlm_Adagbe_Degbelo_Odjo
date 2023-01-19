package fi.eilco.android_project_gestion_film.models

class UserModel (

    var username: String="Crispussia",
    var password:String="Cris@2000",
    //var liked: List<Int> = listOf()
    var liked: MutableList<Int> = mutableListOf()
)