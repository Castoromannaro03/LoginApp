package com.example.InsubriApp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

//Activity contenente login/registrazione
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val dbUtente = Firebase.firestore
    val Utente = dbUtente.collection("Utente")
    lateinit var query : Query


    //Per Google Auth
    private lateinit var mAuth: FirebaseAuth
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private var loginIsLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inizializziamo l'autenticazione di Firebase
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Per ottenere una reference all'account loggato
        mAuth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.loginButton)
        //Imposto il Listener per il pulsante di login
        loginButton.setOnClickListener {
            //Per non far aprire più pagine di login
            if (loginIsLoading) {
                loginIsLoading = false
                onClickListener()
            }
        }

        val signInButton = findViewById<ImageButton>(R.id.googleButton)
        //Imposto il Listener per il pulsante di registrazione
        signInButton.setOnClickListener {
            signIn()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    public override fun onStart() {
        super.onStart()
        //Controllo se l'utente è loggato e nel caso aggiorna
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    //Funzione per ricaricare la pagina in base ai dati inseriti dall'utente
    private fun reload() {
        val currentUser = mAuth.currentUser
        //Controlla se l'utente è già loggato
        if (Firebase.auth.currentUser != null) {

            //Faccio una richiesta al Firestore prendendo le informazioni dell'utente loggato usando come chiave per la ricerca la mail
            query = Utente.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())

            val risultato = query.get().addOnSuccessListener {result ->

                //Se l'utente ha già inserito tutti i suoi dati, passa alla schermata home
                if(result.documents.size > 0) {
                    Log.v("Risultato query", result.documents.get(0).get("Username").toString())
                    val intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                } //Se l'utente non ha mai inserito i suoi dati, passa alla schermata di primo accesso
                else if (result.documents.size == 0) {

                    Log.v("Risultato query", "Non è registrato")
                    startActivity(Intent(this, FirstAccess::class.java))
                    finish()

                }
            }.addOnFailureListener {
                Log.v("Risultato query", "Non ha avuto successo")

            }


        }

    }

    //Funzione che viene richiamata quando viene premuto il pulsante di login
    fun onClickListener(){
        val email = findViewById<TextView>(R.id.EmailAddress)
        //stampo nel log per vedere se prende la stringa giusta
        val password = findViewById<TextView>(R.id.TextPassword)

        //Per prima cosa controllo se l'utente ha inserito qualche carattere
        if(email.text.toString()!="" && password.text.toString()!=""){
            //Utilizzando questa funzione trasmetto i dati del login nel Firebase
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //Login riuscito e aggiorna la UI
                        Toast.makeText(baseContext, "Login riuscito", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                        loginIsLoading=true
                    } else {
                        //Se il login non avviene con successo, mostra un messaggio di errore
                        Toast.makeText(baseContext, "Login non riuscito", Toast.LENGTH_SHORT,).show()
                        updateUI(null)

                    }

                }
        } else {

            Toast.makeText(baseContext, "Inserire dei dati", Toast.LENGTH_SHORT).show()

        }

    }

    //Aggiorno la UI in base ai dati dell'utente
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            reload()
        } else {
            Toast.makeText(this, "Non sei registrato", Toast.LENGTH_LONG).show()
            loginIsLoading=true
        }
    }

    //Funzione per andare alla schermata di Registrazione
    fun goToRegistrationActivity(view: View){
        startActivity(Intent(this,Registration::class.java))
    }

    //Funzione per accedere con l'account Google
    private fun signIn() {
        //Variabile che contiene l'email dell'account Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Funzione per avere una referenza al client che ha come email la variabile gso
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        //Apro la schermata di accesso con Google
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //Funzione che gestisce i dati "ritornati" dal login con Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Login con Google non riuscito", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Funzione per gestire l'accesso all'applicazione con Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Hai fatto l'accesso come ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    Toast.makeText(this, "Autenticazione fallita", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Funzione che manda alla schermata di Recupero Password
    fun gotoRestorePassword(view: View) {

        startActivity(Intent(this, RestorePassword::class.java))

    }
}

