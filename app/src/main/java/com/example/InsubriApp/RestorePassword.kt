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

//Activity per il cambio password
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

    //Funzione che viene richiamata quando viene premuto il pulsante per il recupero password
    fun restorePassword(view: View) {

        //Funzione che manda una mail alla mail inserita nel campo
        auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {

            //Quando viene completata l'operazione, torna automaticamente alla schermata di Login
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } .addOnFailureListener{

            Toast.makeText(baseContext, "Email non valida", Toast.LENGTH_SHORT).show()

        }

    }
}