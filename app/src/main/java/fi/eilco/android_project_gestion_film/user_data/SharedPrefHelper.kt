package fi.eilco.android_project_gestion_film.user_data

import android.content.Context

class SharedPrefHelper(context: Context) {
    /*
    With this class you can basically share any king of primitive data (String, Int, Boolean) through
    all activities of our application. You just have to use them like session variables
     */

    private val sharedPref = context.getSharedPreferences("MySharedPref to store user data", Context.MODE_PRIVATE)

    fun getString(key: String): String? {
        return sharedPref.getString(key, "null") // returns null as a string when the data is not available
    }

    /* */
    fun putString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, -1) // returns -1 when the data is not available
    }

    fun putInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }
}
