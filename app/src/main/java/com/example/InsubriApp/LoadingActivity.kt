package com.example.InsubriApp

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

//Activity di caricamento
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


    //Funzione che gestisce l'apertura di altre pagine
    private fun reload() {
        val currentUser = mAuth.currentUser
        auth = Firebase.auth

        //Verifica se l'utente è loggato
        if (currentUser != null) {

            val risultato = query.get().addOnSuccessListener {result ->

                //Verifica se l'utente ha già inserito tutti i suoi dati personali, e nel caso porta alla Home
                if(result.documents.size > 0) {
                    val intent = Intent(this, NavigationActivity::class.java)
                    finish()
                    startActivity(intent)
                } //Se l'utente non ha ancora completato la registrazione, passa alla schermata di First Access
                else if (result.documents.size == 0) {

                    Log.v("Risultato query LoadingActivity", "Non è registrato")
                    finish()
                    startActivity(Intent(this, FirstAccess::class.java))

                }
            }.addOnFailureListener {
                Log.v("Risultato query", "Non ha avuto successo")

            }



        } //Se l'utente non è ancora loggato, torna alla schermata di Login
        else {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

}