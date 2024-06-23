package com.example.InsubriApp

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.SelectedpostFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

//Fragment del post selezionato
class SelectedPostFragment : Fragment(R.layout.selectedpost_fragment) {

    private var _binding: SelectedpostFragmentBinding? = null
    private val binding get() = _binding!!

    private var emailSelectedPost  = String()
    private var ID = String()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectedpostFragmentBinding.inflate(inflater, container,false)

        val bundle = this.arguments

        var latitudine = 0.0
        var longitudine = 0.0

        //Se il bundle è pieno, prende tutti i dati passati e aggiorna l'UI
        if(bundle!=null){
            binding.textTitolo.text=bundle.getString("Titolo")
            binding.textAutore.text=bundle.getString("Autore")
            binding.textDescrizione.text=bundle.getString("Descrizione")
            latitudine = bundle.getDouble("Latitudine")
            longitudine = bundle.getDouble("Longitudine")
            emailSelectedPost = bundle.getString("Email").toString()
            ID = bundle.getString("ID").toString()
        }

        //Creo un altro bundle per poi passarlo al Map Fragment
        var bundleForMaps = Bundle()

        //Metto nel bundle la latitudine e la longitudine
        bundleForMaps.putDouble("Latitudine", latitudine)
        bundleForMaps.putDouble("Longitudine", longitudine)

        //Setto gli argomenti da passare nel Map Fragment
        val fragmentMap = MapsFragment()
        fragmentMap.setArguments(bundleForMaps)

        //Quando entro nel post selezionato, faccio una "transizione" per caricare la mappa, così da passare anche i dati
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentMap)
            .commit()

        //Per mettere la TextView del nome del creatore del post sottolineato
        binding.textAutore.paintFlags = binding.textAutore.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Funzione per gestire la gesture dello swipe all'indietro, così da tornare alla bacheca
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, NoticeboardFragment())
                        .commit()

                }
            }
        )

        //Se l'utente che ha creato il post è lo stesso di chi lo sta visualizzando, allora compare il button per eliminare il post
        if(emailSelectedPost.equals(Firebase.auth.currentUser?.email)){

            //Rendo visibile il button
            binding.eliminaPostButton.visibility = View.VISIBLE
            //Collego a questo button la funzione eliminaPost
            binding.eliminaPostButton.setOnClickListener {eliminaPost() }
        }
        else{
            binding.eliminaPostButton.visibility = View.GONE
        }

        val view = binding.root
        return view
    }

    //Funzione per eliminare il post
    fun eliminaPost(){
        val bacheca = Firebase.firestore.collection("Bacheca")
        //Dopo aver eliminato il post, torna alla Bacheca
        bacheca.document(ID).delete().addOnSuccessListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, NoticeboardFragment())
                .commit()
        }

    }

}

