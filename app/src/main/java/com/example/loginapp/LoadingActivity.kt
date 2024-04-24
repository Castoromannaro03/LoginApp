package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

class LoadingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val dbUtente = Firebase.firestore
    val Utente = dbUtente.collection("Utente")
    val query = Utente.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        reload()

        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun reload() {
        val currentUser = mAuth.currentUser
        auth = Firebase.auth
        if (currentUser != null) {

            //val query = Utente.whereEqualTo("Cognome", "Fontana")

            val risultato = query.get().addOnSuccessListener {result ->

                if(result.documents.size > 0) {
                    Log.v("Risultato query LoadingActivity", result.documents.get(0).get("Username").toString())
                    val intent = Intent(this, NavigationActivity::class.java)
                    finish()
                    startActivity(intent)
                } else if (result.documents.size == 0) {

                    Log.v("Risultato query LoadingActivity", "Non è registrato")

                    finish()
                    startActivity(Intent(this, FirstAccess::class.java))

                }
            }.addOnFailureListener {
                Log.v("Risultato query", "Non ha avuto successo")

            }



        } else {

            Log.v("Evidentemente un errore", auth.currentUser?.email.toString())
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

}