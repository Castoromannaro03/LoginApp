package com.example.InsubriApp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.ProfileFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore


class ProfileFragment : Fragment(R.layout.profile_fragment) {

    val databaseUser = Firebase.firestore
    val User = databaseUser.collection("Utente")

    lateinit var Username : String
    lateinit var Nome : String
    lateinit var Cognome : String
    lateinit var Email : String

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

        val bundle = this.arguments

        if(bundle!=null){
            Nome = bundle.getString("Nome").toString()
            Cognome = bundle.getString("Cognome").toString()
            Username = bundle.getString("Username").toString()
            Email = bundle.getString("Email").toString()
            binding.nomeUser.text = Nome
            binding.cognomeUser.text = Cognome
            binding.usernameUser.text = Username
            binding.emailUser.text = Email

        }

        return view
    }



}