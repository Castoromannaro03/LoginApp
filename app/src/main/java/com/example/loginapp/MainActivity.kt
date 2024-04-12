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


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        finish();
        startActivity(Intent(this,LogOut::class.java))
    }

    fun onClickListener(view: View){
        val email = findViewById<TextView>(R.id.EmailAddress)
        //stampo nel log per vedere se prende la stringa giusta
        Log.v("error", email.text.toString())
        val password = findViewById<TextView>(R.id.TextPassword)

        if(email.text.toString()!="" && password.text.toString()!=""){
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "Login riuscito", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)

                    }

                }
        }

    }

    //Change UI according to user data.
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LogOut::class.java))
            finish()
        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }

    fun goToRegistrationActivity(view: View){
        startActivity(Intent(this,Registration::class.java))
    }
}