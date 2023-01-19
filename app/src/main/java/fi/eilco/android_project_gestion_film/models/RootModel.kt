package fi.eilco.android_project_gestion_film.models

class RootModel (
    var page:Int=0,
    var results :ArrayList<MovieModel> =ArrayList(),
    var total_pages:Int=0,
    var total_results:Int=0

)