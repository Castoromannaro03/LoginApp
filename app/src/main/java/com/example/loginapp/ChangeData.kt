package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

class ChangeData : AppCompatActivity() {

    //Definisco e inizializzo le variabili che si riferiscono agli EditText così da poterle usare in tutta la classe
    val nome : EditText by lazy {

        findViewById(R.id.editTextNome)

    }

    val cognome : EditText by lazy {

        findViewById(R.id.editTextCognome)

    }

    val username : EditText by lazy {

        findViewById(R.id.editTextUsername)

    }


    //Creo un riferimento al database Firebase
    val db = Firebase.firestore
    //Salvo in una variabile l'elenco delle mail registrate, che si trovano nella raccolta "Utente"
    val utente = db.collection("Utente")

    //Effettuo una query sul database dove sono salvati i dati utente e cerco in base alla mail con la quale l'utente è registrato
    var queryUser = utente.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val risultato = queryUser.get().addOnSuccessListener { result ->

            nome.setHint(result.documents[0].get("Nome").toString())
            cognome.setHint(result.documents[0].get("Cognome").toString())
            username.setHint(result.documents[0].get("Username").toString())

        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_change_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Creo una funzione per tornare al ProfileFragment se non si vogliono modificare i datu
    fun goBack(view: View) {

        startActivity(Intent(this, NavigationActivity::class.java))

    }

    fun confermaDati(view: View) {

        //Prendiamo i dati inseriti dall'utente e li salviamo in una variabile
        var nomeTV = nome.text
        var cognomeTV = cognome.text
        var usernameTV = username.text


        //Controllo se l'utente ha effettivamente inserito dei dati nei campi
        if (nomeTV.length > 0 && cognomeTV.length > 0 && usernameTV.length > 0) {

            //Salvo il risultato della query in una variabile
            val risultato = queryUser.get().addOnSuccessListener { result ->

                //Creo un set di dati dove salverò i nuovi dati inseriti dall'utente
                val setData = hashMapOf(
                    "Nome" to nomeTV.toString(),
                    "Cognome" to cognomeTV.toString(),
                    "Username" to usernameTV.toString()
                )

                //Sovrascrivo tutti i dati nuovi, usando come "criterio di ricerca" la mail con cui è registrato all'utente
                utente.document(Firebase.auth.currentUser?.email.toString()).set(setData).addOnCompleteListener{

                    //Dopo aver salvato i nuovi dati, torno alla ProfileFragment
                    startActivity(Intent(this, NavigationActivity::class.java))
                    finish()

                }

            }

        }
    }
}