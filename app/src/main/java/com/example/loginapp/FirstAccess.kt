package com.example.loginapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class FirstAccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_access)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onClickConferma(view: View){
        var nome = findViewById<TextView>(R.id.editTextNome).text
        var cognome = findViewById<TextView>(R.id.editTextCognome).text
        var username = findViewById<TextView>(R.id.editTextUsername).text

        if(nome!=null && cognome != null && username!=null){
            val db = Firebase.firestore
            val utente = db.collection("Utente")


            val data = hashMapOf(
                "Cognome" to nome,
                "Nome" to cognome,
                "Username" to username
            )

            utente.document(Firebase.auth.currentUser?.email.toString()).set(data)
        }
    }
}