package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loginapp.databinding.ProfileFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore


class ProfileFragment : Fragment(R.layout.profile_fragment) {

    val databaseUser = Firebase.firestore
    val User = databaseUser.collection("Utente")

    private var _binding: ProfileFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        var queryUser = User.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())




        val risultato = queryUser.get().addOnSuccessListener {result ->

            var nome = result.documents[0].get("Nome")
            var cognome = result.documents[0].get("Cognome")
            var username = result.documents[0].get("Username")

            binding.nomeUser.text = nome.toString()
            binding.cognomeUser.text = cognome.toString()
            binding.usernameUser.text = username.toString()
            binding.emailUser.text = Firebase.auth.currentUser?.email.toString()

        }.addOnFailureListener{

            Log.v("Failure", "Ha fallito")

        }

        return view
    }



}