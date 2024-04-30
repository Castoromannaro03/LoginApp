package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RestorePassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    val email : EditText by lazy {

        findViewById(R.id.EmailAddress)

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restore_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun restorePassword(view: View) {

        auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } .addOnFailureListener{

            Toast.makeText(baseContext, "Email non valida", Toast.LENGTH_SHORT).show()

        }

    }
}