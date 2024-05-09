package com.example.InsubriApp

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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

class AddPostActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val bacheca = db.collection("Bacheca")
    val utente = db.collection("Utente")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                /**
                 * onBackPressed logic goes here. For instance:
                 * Prevents closing the app to go home screen when in the
                 * middle of entering data to a form
                 * or from accidentally leaving a fragment with a WebView in it
                 *
                 * Unregistering the callback to stop intercepting the back gesture:
                 * When the user transitions to the topmost screen (activity, fragment)
                 * in the BackStack, unregister the callback by using
                 * OnBackInvokeDispatcher.unregisterOnBackInvokedCallback
                 * (https://developer.android.com/reference/kotlin/android/window/OnBackInvokedDispatcher#unregisteronbackinvokedcallback)
                 */
                startActivity(Intent(baseContext, NavigationActivity::class.java))
                finish()

            }
        }
        else{
            this.onBackPressedDispatcher.addCallback {
                startActivity(Intent(baseContext, NavigationActivity::class.java))
            }
        }

         */

    }

    fun conferma(view : View){
        var titolo = findViewById<TextView>(R.id.editTextTitolo)
        var descrizione = findViewById<TextView>(R.id.editTextDescrizione)


        if(titolo.length()>0 && descrizione.length()>0){
            val textTitolo = titolo.text
            val textDescrizione = descrizione.text
            Toast.makeText(this.baseContext, "Chiusura pagina", Toast.LENGTH_LONG).show()
            val queryUtente = utente.whereEqualTo(FieldPath.documentId(),
                FirebaseAuth.getInstance().currentUser?.email
            )
            queryUtente.get().addOnSuccessListener { result->

                val username = result.documents.get(0).get("Username")


                val data = hashMapOf(
                    "Titolo" to textTitolo.toString(),
                    "Descrizione" to textDescrizione.toString(),
                    "Autore" to username.toString()
                )
                bacheca.document().set(data).addOnCompleteListener{
                    Toast.makeText(this.baseContext, "Chiusura pagina", Toast.LENGTH_LONG).show()
                    Log.v("Log di prova", "prova")
                    finish()
                }
            }.addOnFailureListener{
                Toast.makeText(this.baseContext, "Recupero dati non riuscito", Toast.LENGTH_LONG).show()
            }



        }
        else{
            Toast.makeText(this.baseContext, "Valori inseriti non validi", Toast.LENGTH_LONG).show()
        }
    }

}