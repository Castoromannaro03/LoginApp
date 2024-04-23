package com.example.loginapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment(R.layout.home_fragment) {

    fun paolo(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }

    companion object {
        fun paolo() {

            val db = Firebase.firestore
            val utente = db.collection("Utente")

            val data1 = hashMapOf(
                "Cognome" to "Fontana",
                "Nome" to "Federico",
                "Username" to "CastoroMannaro03"
            )

            db.collection("Utente").add(data1)
        }
    }


}