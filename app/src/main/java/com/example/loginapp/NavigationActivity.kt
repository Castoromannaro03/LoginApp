package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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

    //Richiamo i Fragment
    val noticeboardFragment = NoticeboardFragment()
    val searchFragment = SearchFragment()
    val chatFragment = ChatFragment()
    val profileFragment = ProfileFragment()

    //Definisco e inizializzo le variabil che si riferiscono ai Button
    val homeButton : ImageButton by lazy {

        findViewById(R.id.homeButton)

    }

    val searchButton : ImageButton by lazy {

        findViewById(R.id.searchButton)

    }

    val chatButton : ImageButton by lazy {

        findViewById(R.id.chatButton)

    }

    val profileButton : ImageButton by lazy {

        findViewById(R.id.profileButton)

    }


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


        /*
        while(!risultato.isComplete){

        }
         */
        //startActivity(Intent(this, FirstAccess::class.java))
        //finish()
    }



    fun logout(view: View){
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


    //PROVARE A FARE UNA FUNZIONE UNICA, PASSANDO COME OGGETTO TUTTI I BUTTON
    fun setDefault() {

        homeButton.setImageResource(R.drawable.homelogo)
        searchButton.setImageResource(R.drawable.lentelogo)
        chatButton.setImageResource(R.drawable.chatlogo)
        profileButton.setImageResource(R.drawable.profilologo)

    }


    fun toHome(view: View){

        setDefault()
        homeButton.setImageResource(R.drawable.homelogo_premuto)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, noticeboardFragment).commit()

    }

    fun toSearch(view: View){

        setDefault()
        searchButton.setImageResource(R.drawable.lentelogo_premuto)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, searchFragment).commit()

    }

    fun toChat(view: View){

        setDefault()
        chatButton.setImageResource(R.drawable.chatlogo_premuto)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, chatFragment).commit()

    }

    fun toProfile(view: View){

        setDefault()
        profileButton.setImageResource(R.drawable.profilologo_premuto)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit()

    }

    //Funzione che uso nel profile_fragment.xml per andare all'activity ChangeData
    fun gotoChangeData(view: View) {

        startActivity(Intent(this, ChangeData::class.java))

    }

}