package com.example.InsubriApp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

//Activity per l'aggiunta di un post alla bacheca
class AddPostActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val bacheca = db.collection("Bacheca")
    val utente = db.collection("Utente")
    val FINE_PERMISSION_CODE = 1
    var currentLocation : Location = Location("nullo")
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    //Funzione per ottenere la posizione attuale dell'utente
    fun getLastLocation() {

        //Controllo i permessi per accedere alla posizione
        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        }

        var task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {location ->

            //Se trova la posizione, la salva in una variabile
            if(location != null) {
                currentLocation = location
            }

        //Se non trova la posizione, mostra messaggio d'errore
        }.addOnFailureListener{
            Toast.makeText(baseContext, "Segnale GPS non trovato, accendi il GPS e riavvia l'app", Toast.LENGTH_LONG).show()
        }
    }

    //Funzione per salvare e postare il post
    fun conferma(view : View){
        var titolo = findViewById<TextView>(R.id.editTextTitolo)
        var descrizione = findViewById<TextView>(R.id.editTextDescrizione)

        //Chiede i permessi per accedere alla posizione
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        //Controlla se l'utente ha inserito i dati e se c'è una posizione valida
        if(titolo.length()>0 && descrizione.length()>0 && currentLocation.provider!="nullo"){
            //Prendo le informazione inserite dall'utente che verranno poi salvate nel Firestore
            val textTitolo = titolo.text
            val textDescrizione = descrizione.text
            Toast.makeText(this.baseContext, "Chiusura pagina", Toast.LENGTH_LONG).show()
            val queryUtente = utente.whereEqualTo(FieldPath.documentId(),
                FirebaseAuth.getInstance().currentUser?.email
            )
            queryUtente.get().addOnSuccessListener { result->

                val username = result.documents.get(0).get("Username")

                //Creo una hashmap di dati che verrà salvata nel Firestore
                val data = hashMapOf(
                    "Titolo" to textTitolo.toString(),
                    "Descrizione" to textDescrizione.toString(),
                    "Autore" to username.toString(),
                    "Latitudine" to currentLocation.latitude,
                    "Longitudine" to currentLocation.longitude,
                    "Email" to Firebase.auth.currentUser!!.email
                )
                //Salvo i dati nel Firestore e chiudo la schermata
                bacheca.document().set(data).addOnCompleteListener{
                    Toast.makeText(this.baseContext, "Chiusura pagina", Toast.LENGTH_LONG).show()
                    finish()
                }
            }.addOnFailureListener{
                Toast.makeText(this.baseContext, "Recupero dati non riuscito", Toast.LENGTH_LONG).show()
            }



        }
        else if(currentLocation.provider=="nullo"){
            Toast.makeText(baseContext, "Segnale GPS non trovato, accendi il GPS e riavvia l'app", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this.baseContext, "Valori inseriti non validi", Toast.LENGTH_LONG).show()
        }
    }

}