package fi.eilco.android_project_gestion_film

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fi.eilco.android_project_gestion_film.models.UserModel
import fi.eilco.android_project_gestion_film.user_data.SharedPrefHelper

class Register : AppCompatActivity() {
    val databaseRef = FirebaseDatabase.getInstance().getReference("users")

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Go back to the previous activity when clicking on back_arrow
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener{
            finish()
        }

        val username=findViewById<TextView>(R.id.username_input)
        val password=findViewById<TextView>(R.id.password_input)
        val conpassword=findViewById<TextView>(R.id.confirm_password_input)
        //charger notre plant repository
        val registerButton=findViewById<TextView>(R.id.registerBtn)
        val LoginNowButton=findViewById<TextView>(R.id.loginNow)
        val user= UserModel()

        /* Beginning of Testing SharedPrerences for session variables */
        sharedPrefHelper = SharedPrefHelper(this)
        sharedPrefHelper.clearSharedPreferences()
        val sessionVariable = sharedPrefHelper.getString("session_variable") // -1 is returned when the session variable is not found
        Log.d("Session Variable testing", "REGISTER_SESSIONNNNNNNNN " + sessionVariable)
        /* End of Testing SharedPrerences for session variables */

        registerButton.setOnClickListener{

            //get data from edittexts
            val usernameText=username.text.toString()

            val passwordText=password.text.toString()
            val conPasswordText=conpassword.text.toString()
            user.username=usernameText
            user.password=passwordText
            //verifier que tous les champs sont remplis
            //verifier que tous les champs sont remplis
            if(usernameText.isEmpty()||passwordText.isEmpty()||conPasswordText.isEmpty()){
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            //check if passwords are matching with each other
            else if(!passwordText.equals(conPasswordText)){
                Toast.makeText(this,"Password are not matching", Toast.LENGTH_SHORT).show()
            }else {
                //sending data to firebase
                //we use username as unique identity of every user
                //sending data to firebase
                //we use username as unique identity of every user
                databaseRef.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //create user in database

                        if(snapshot.hasChild(usernameText)){
                            Toast.makeText(this@Register,"User registered error", Toast.LENGTH_SHORT).show()
                        }else {
                            databaseRef.child(usernameText).child("username").setValue(user.username)
                            databaseRef.child(usernameText).child("password").setValue(user.password)
                            databaseRef.child(usernameText).child("liked").setValue(user.liked)
                            Toast.makeText(this@Register,"User registered successfully", Toast.LENGTH_SHORT).show()
                            val intentLogin = Intent(this@Register, Login::class.java)
                            intentLogin.apply {
                                startActivity(this)
                            }
                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }

                )
            }

        }
        LoginNowButton.setOnClickListener{
            val intentLogin = Intent(this, Login::class.java)
            intentLogin.apply {
                startActivity(this)
            }
        }
    }
}