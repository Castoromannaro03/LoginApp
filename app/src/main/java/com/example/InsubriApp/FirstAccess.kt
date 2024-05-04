package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class FirstAccess : AppCompatActivity() {

    private val sceltaFacolta : Spinner by lazy {

        findViewById(R.id.spinner)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_access)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val arrayList = ArrayList<String>()
        arrayList.add("Scegliere la Facoltà")
        arrayList.add("Nessuna Facoltà")
        arrayList.add("Biotecnologie")
        arrayList.add("Chimica e chimica industriale")
        arrayList.add("Economia e management")
        arrayList.add("Educazione professionale")
        arrayList.add("Fisica")
        arrayList.add("Fisioterapia")
        arrayList.add("Igiene dentale")
        arrayList.add("Infermieristica")
        arrayList.add("Informatica (la migliore)")
        arrayList.add("Ingegneria per la sicurezza")
        arrayList.add("Matematica")
        arrayList.add("Ostetrica")
        arrayList.add("Scienze biologiche")
        arrayList.add("Scienze del turismo")
        arrayList.add("Scienze dell'ambiente e della natura")
        arrayList.add("Scienze della comunicazione")
        arrayList.add("Scienze della mediazione")
        arrayList.add("Scienze motorie")
        arrayList.add("Storia del mondo contemporaneo")
        arrayList.add("Tecniche della prevenzione")
        arrayList.add("Tecniche di laboratorio biomedico")
        arrayList.add("Tecniche di radiologia medica")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sceltaFacolta.adapter = arrayAdapter

    }

    fun onClickConferma(view: View){
        var nome = findViewById<TextView>(R.id.editTextNome).text
        var cognome = findViewById<TextView>(R.id.editTextCognome).text
        var username = findViewById<TextView>(R.id.editTextUsername).text
        var facolta = sceltaFacolta.selectedItem


            if (nome.length > 0 && cognome.length > 0 && username.length > 0 && facolta != "Scegliere la Facoltà") {
                val db = Firebase.firestore
                val utente = db.collection("Utente")


                val data = hashMapOf(
                    "Cognome" to cognome.toString(),
                    "Nome" to nome.toString(),
                    "Username" to username.toString(),
                    "Facoltà" to facolta
                )

                utente.document(Firebase.auth.currentUser?.email.toString()).set(data)
                    .addOnCompleteListener {

                        Log.v("Entrato", "Complete Listener")
                        startActivity(Intent(this, NavigationActivity::class.java))
                        finish()

                    }
            }

    }


    fun logout(view: View){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        Toast.makeText(baseContext, "logout", Toast.LENGTH_LONG).show()

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