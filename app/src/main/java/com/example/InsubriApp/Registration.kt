package com.example.InsubriApp

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

//Activitu per la registrazione dell'utente
class Registration : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Funzione che viene richiamata quando viene premuto il pulsante per la registrazione
    fun registration(view: View){
        val email = findViewById<TextView>(R.id.RegistrationEmailAddress)
        val password = findViewById<TextView>(R.id.RegistrationPassword)
        val emailRepeat = findViewById<TextView>(R.id.RegistrationEmailAddressRepeat)
        val passwordRepeat = findViewById<TextView>(R.id.RegistrationPasswordRepeat)

        //Controllo se l'utente ha inserito qualcosa nei campi
        if(email.text.toString()!="" && password!=null){
            //Controllo se mail e password coincidono
            if(emailRepeat.text.toString() == email.text.toString() && passwordRepeat.text.toString() == password.text.toString()) {
                //Richiamo questa funzione per passare mail e password al Firebase
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Registrazione effettuata e aggiorno la UI
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            //Se la registrazione non va a buon fine, mostro un messaggio di errore
                            Toast.makeText(baseContext, "Autenticazione fallita", Toast.LENGTH_SHORT,).show()
                            updateUI(null)
                        }
                    }
            } else {

                Toast.makeText(baseContext, "I valori dell'Email o Password non corrispondono", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(baseContext, "Email o Password non validi", Toast.LENGTH_SHORT).show()
            //Log.v("errore", "entro nel metodo")
        }

    }

    //Aggiorno la UI in base ai dati dell'utente
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "Ti sei registrato con successo", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, FirstAccess::class.java))
            finish()
        } else {
            Toast.makeText(this, "Non sei registrato", Toast.LENGTH_LONG).show()
        }
    }

    //Funzione che viene richiamata quando viene premuto il pulsante Indietro, tornando alla schermata di Login
    fun goBack(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}