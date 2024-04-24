package com.example.loginapp

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
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Per Google Auth
        mAuth = FirebaseAuth.getInstance()




        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (loginIsLoading) {
                loginIsLoading = false
                onClickListener()
            }
        }

        val signInButton = findViewById<ImageButton>(R.id.googleButton)
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
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        val currentUser = mAuth.currentUser
        if (Firebase.auth.currentUser != null) {

            query = Utente.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())

            val risultato = query.get().addOnSuccessListener {result ->

                if(result.documents.size > 0) {
                    Log.v("Risultato query", result.documents.get(0).get("Username").toString())
                    val intent = Intent(this, NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (result.documents.size == 0) {

                    Log.v("Risultato query", "Non è registrato")
                    startActivity(Intent(this, FirstAccess::class.java))
                    finish()

                }
            }.addOnFailureListener {
                Log.v("Risultato query", "Non ha avuto successo")

            }


        }

    }

    fun onClickListener(){
        val email = findViewById<TextView>(R.id.EmailAddress)
        //stampo nel log per vedere se prende la stringa giusta
        val password = findViewById<TextView>(R.id.TextPassword)

        if(email.text.toString()!="" && password.text.toString()!=""){
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "Login riuscito", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                        loginIsLoading=true
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)

                    }

                }
        } else {

            Toast.makeText(baseContext, "Inserire dei dati", Toast.LENGTH_SHORT).show()

        }

    }

    //Change UI according to user data.
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show()
            reload()
        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show()
            loginIsLoading=true
        }
    }

    fun goToRegistrationActivity(view: View){
        startActivity(Intent(this,Registration::class.java))
    }

    //Per Google Auth
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

