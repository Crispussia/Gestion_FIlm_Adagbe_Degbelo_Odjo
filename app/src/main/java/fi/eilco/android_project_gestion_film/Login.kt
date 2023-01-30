package fi.eilco.android_project_gestion_film

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fi.eilco.android_project_gestion_film.user_data.SharedPrefHelper

class Login : AppCompatActivity() {
    val databaseRef = FirebaseDatabase.getInstance().getReference("users")

    private lateinit var sharedPrefHelper: SharedPrefHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton=findViewById<Button>(R.id.loginBtn)
        val registerButton=findViewById<TextView>(R.id.registerNowBtn)
        val username=findViewById<TextView>(R.id.username_input)
        val password=findViewById<TextView>(R.id.password_input)

        /* Beginning of Testing SharedPrerences for session variables */
        sharedPrefHelper = SharedPrefHelper(this)
        val sessionVariable = sharedPrefHelper.getString("session_variable") // -1 is returned when the session variable is not found
        Log.d("Session Variable testing", "LOGIN_SESSIONNNNNNNNN " + sessionVariable)
        /* End of Testing SharedPrerences for session variables */



        loginButton.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()


            if (usernameText.isEmpty()||passwordText.isEmpty()){
                Toast.makeText(this,"Please enter your username or password", Toast.LENGTH_SHORT).show()
            }else {
                //Log.d("Log", usernameText)
                databaseRef.child(usernameText).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //check if username exists
                        if (snapshot.exists()) {
                            Log.d("Sucess", snapshot.toString())
                            //now get password of user from firebase data and match it with user entered
                            val getPassword = snapshot.child("password").getValue().toString()
                            if (getPassword.equals(passwordText)) {
                                Toast.makeText(
                                    this@Login,
                                    "Successfull Logged in",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intentMain = Intent(this@Login, MainActivity::class.java)
                                intentMain.apply {
                                    putExtra("username",usernameText)
                                    startActivity(this)
                                }
                            } else {
                                Toast.makeText(this@Login, "Wrong Password", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }else{
                            Toast.makeText(this@Login, "Wrong username", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Error Database", error.toString())
                        TODO("Not yet implemented")
                    }

                })


            }
        }
        registerButton.setOnClickListener{
            val intentRegister = Intent(this, Register::class.java)
            intentRegister.apply {

                startActivity(this)
            }


        }
    }
}