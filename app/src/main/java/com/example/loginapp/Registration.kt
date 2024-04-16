package com.example.loginapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class Registration : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun registration(view: View){
        //Toast.makeText(baseContext, "Entro", Toast.LENGTH_LONG).show()
        val email = findViewById<TextView>(R.id.RegistrationEmailAddress)
        val password = findViewById<TextView>(R.id.RegistrationPassword)
        val emailRepeat = findViewById<TextView>(R.id.RegistrationEmailAddressRepeat)
        val passwordRepeat = findViewById<TextView>(R.id.RegistrationPasswordRepeat)
        Log.v("errore", email.text.toString())
        //Toast.makeText(baseContext, "Entro", Toast.LENGTH_LONG).show()

        if(email.text.toString()!="" && password!=null){
            if(emailRepeat.text.toString() == email.text.toString() && passwordRepeat.text.toString() == password.text.toString()) {
                Log.v("errore", "entro nel metodo")
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            updateUI(null)
                        }
                    }
            } else {

                Toast.makeText(baseContext, "I valori dell'Email o Password non corrispondono", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(baseContext, "Email o Password non validi", Toast.LENGTH_SHORT).show()
            //Log.v("errore", "entro nel metodo")
        }

    }

    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LogOut::class.java))
            finish()
        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }

    fun goBack(view: View) {

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}