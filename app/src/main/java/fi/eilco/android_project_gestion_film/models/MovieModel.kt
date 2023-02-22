package fi.eilco.android_project_gestion_film.models

class MovieModel(
    var original_title: String = "",
    var overview: String = "",
    var poster_path: String = "",
    var release_date: String = "",
    var vote_average: String = "",
    var vote_count: String = "",
    var imageLink: String = "",
    var rate: String = "Rate : ",
    var votes: String = "Votes : ",
    var id:Int=0,
    var popularity:String=""




) {
    override fun toString(): String {
        return "MovieModel(original_title='$original_title', overview='$overview', poster_path='$poster_path', release_date='$release_date', vote_average='$vote_average', vote_count='$vote_count', imageLink='$imageLink', rate='$rate', votes='$votes', id=$id, popularity='$popularity')"
    }
}
