package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await

class NavigationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dbUtente = Firebase.firestore
        val Utente = dbUtente.collection("Utente")

        val query = Utente.whereEqualTo(FieldPath.documentId(), auth.currentUser?.email.toString())
        //val query = Utente.whereEqualTo("Cognome", "Fontana")

        val risultato = query.get().addOnSuccessListener {result ->

            Log.v("Risultato query", result.documents.get(0).get("Username").toString())
        }.addOnFailureListener {
            Log.v("Risultato query", "Non ha avuto successo")

        }
        /*
        while(!risultato.isComplete){

        }
         */
        //startActivity(Intent(this, FirstAccess::class.java))
        //finish()
    }



    fun logout(view: View){
        Toast.makeText(baseContext, "logout", Toast.LENGTH_LONG).show()
        Log.v("errore", "pop up non visualizzato")

        Firebase.auth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this, MainActivity::class.java)

        }

        //auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}