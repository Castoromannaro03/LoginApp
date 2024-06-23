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

//Fragment che mostra le informazioni dell'utente
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    val databaseUser = Firebase.firestore
    val User = databaseUser.collection("Utente")

    lateinit var Username : String
    lateinit var Nome : String
    lateinit var Cognome : String
    lateinit var Email : String
    lateinit var Facolta : String

    //Variabili che servono per creare delle reference agli oggetti dell'XML del fragment
    private var _binding: ProfileFragmentBinding? = null
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

        //Prendo i dati dal bundle ricevuti dalla NavigationActivity e aggiorno la UI
        if(bundle!=null){
            Nome = bundle.getString("Nome").toString()
            Cognome = bundle.getString("Cognome").toString()
            Username = bundle.getString("Username").toString()
            Facolta = bundle.getString("Facoltà").toString()
            Email = bundle.getString("Email").toString()
            binding.nomeUser.text = Nome
            binding.cognomeUser.text = Cognome
            binding.usernameUser.text = Username
            binding.facoltaUser.text = Facolta
            binding.emailUser.text = Email

        }

        return view
    }


}