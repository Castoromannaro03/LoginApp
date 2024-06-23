package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

//Activity per modificare i dati utente
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

    private val facolta : Spinner by lazy {

        findViewById(R.id.spinner)

    }



    //Creo un riferimento al database Firebase
    val db = Firebase.firestore
    //Salvo in una variabile l'elenco delle mail registrate, che si trovano nella raccolta "Utente"
    val utente = db.collection("Utente")

    //Effettuo una query sul database dove sono salvati i dati utente e cerco in base alla mail con la quale l'utente è registrato
    var queryUser = utente.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val arrayList = ArrayList<String>()

        var facoltaCorrente = ""

        //Prendo le informazioni dal Firebase e le metto come "hint" nelle caselle di testo
        val risultato = queryUser.get().addOnSuccessListener { result ->

            nome.setHint(result.documents[0].get("Nome").toString())
            cognome.setHint(result.documents[0].get("Cognome").toString())
            username.setHint(result.documents[0].get("Username").toString())
            facoltaCorrente = result.documents[0].get("Facoltà").toString()

        }.addOnCompleteListener() {

            //Aggiungo all'arrayList la lista di tutte le facoltà
            arrayList.add(0, facoltaCorrente + "")
            arrayList.add(1, "Nessuna Facoltà")
            arrayList.add(2, "Biotecnologie")
            arrayList.add(3, "Chimica e chimica industriale")
            arrayList.add(4, "Economia e management")
            arrayList.add(5, "Educazione professionale")
            arrayList.add(6, "Fisica")
            arrayList.add(7, "Fisioterapia")
            arrayList.add(8, "Igiene dentale")
            arrayList.add(9, "Infermieristica")
            arrayList.add(10, "Informatica (la migliore)")
            arrayList.add(11, "Ingegneria per la sicurezza")
            arrayList.add(12, "Matematica")
            arrayList.add(13, "Ostetrica")
            arrayList.add(14, "Scienze biologiche")
            arrayList.add(15, "Scienze del turismo")
            arrayList.add(16, "Scienze dell'ambiente e della natura")
            arrayList.add(17, "Scienze della comunicazione")
            arrayList.add(18, "Scienze della mediazione")
            arrayList.add(19, "Scienze motorie")
            arrayList.add(20, "Storia del mondo contemporaneo")
            arrayList.add(21, "Tecniche della prevenzione")
            arrayList.add(22, "Tecniche di laboratorio biomedico")
            arrayList.add(23, "Tecniche di radiologia medica")

            //Rimuovo dalla lista la facoltà corrente dell'utente
            Log.v("devo imprecare", arrayList.indexOf(facoltaCorrente).toString())

            //Passo questa lista di facoltà allo spinner tramite un ArrayAdapter
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            facolta.adapter = arrayAdapter


        }

    }

    //Creo una funzione per tornare al ProfileFragment se non si vogliono modificare i dati
    fun goBack(view: View) {

        startActivity(Intent(this, NavigationActivity::class.java))
        finish()

    }

    //Funzione che viene chiamata quando si vogliono confermare i dati inseriti
    fun confermaDati(view: View) {

        //Prendiamo i dati inseriti dall'utente e li salviamo in una variabile
        var nomeTV = nome.text
        var cognomeTV = cognome.text
        var usernameTV = username.text
        var facoltaTV = facolta.selectedItem


        //Controllo se l'utente ha effettivamente inserito dei dati nei campi
        if (nomeTV.length > 0 && cognomeTV.length > 0 && usernameTV.length > 0) {

            //Salvo il risultato della query in una variabile
            val risultato = queryUser.get().addOnSuccessListener { result ->

                //Creo un set di dati dove salverò i nuovi dati inseriti dall'utente
                val setData = hashMapOf(
                    "Nome" to nomeTV.toString(),
                    "Cognome" to cognomeTV.toString(),
                    "Username" to usernameTV.toString(),
                    "Facoltà" to facoltaTV.toString()
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