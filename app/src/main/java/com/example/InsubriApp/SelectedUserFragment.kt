package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.SelecteduserFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SelectedUserFragment : Fragment(R.layout.selecteduser_fragment){

    private var _binding: SelecteduserFragmentBinding? = null
    private val binding get() = _binding!!

    private val firebase = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelecteduserFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.startChatButton.setOnClickListener{startChat()}

        val bundle = this.arguments

        var usernameSelectedUser=""
        var emailSelectdUser=""
        var nomeSelectedUser=""
        var cognomeSelectedUser=""
        var facoltaSelectedUser =""

        if (bundle != null) {
            usernameSelectedUser = bundle.getString("Username").toString()
            emailSelectdUser = bundle.getString("Email").toString()
            nomeSelectedUser = bundle.getString("Nome").toString()
            facoltaSelectedUser = bundle.getString("Facoltà").toString()
            cognomeSelectedUser = bundle.getString("Cognome").toString()
        }

        binding.usernameUser.text = usernameSelectedUser
        binding.cognomeUser.text = cognomeSelectedUser
        binding.nomeUser.text = nomeSelectedUser
        binding.facoltaUser.text = facoltaSelectedUser
        binding.emailUser.text = emailSelectdUser

        return view
    }

    fun startChat(){
        val elencoChat = firebase.collection("ElencoChat").document(Firebase.auth.currentUser?.email.toString()).collection("Chat")
        //riferimento alla specifica chat con l'utente selezionato dalla ricerca
        val chatUtenteSelezionato = elencoChat.document(binding.usernameUser.text.toString())
        Log.v("Stampa", binding.usernameUser.text.toString())

        if (!binding.usernameUser.text.equals("Caricamento...")){
            Log.v("Stampa", "Entro")
            //val queryChat = elencoChat.whereEqualTo("Document ID", binding.nomeUser.text)
            //val queryChat = elencoChat
            chatUtenteSelezionato.get().addOnSuccessListener {
                Log.v("Stampa", it.exists().toString())
                if(it.exists()){
                    var intent = Intent(this.context, SendMessageActivity::class.java)
                    intent.putExtra("nomeChat", it.get("nomeChat").toString())
                    intent.putExtra("nomeDestinatario", it.id)

                    startActivity(intent)
                }
                else{
                    var nomeUtenteLoggato = String()
                    val queryUtente = Firebase.firestore.collection("Utente").document(Firebase.auth.currentUser!!.email.toString())
                    queryUtente.get().addOnSuccessListener {
                        if(it.exists()){
                            nomeUtenteLoggato = it.get("Username").toString()
                        }

                        val nomeChatGenerato = nomeUtenteLoggato + "-" + binding.usernameUser.text.toString()
                        val data = hashMapOf(
                            "nomeChat" to nomeChatGenerato
                        )
                        chatUtenteSelezionato.set(data).addOnSuccessListener {
                            Log.v("Risultato caricamento", "Caricamento nuova chat nel Firestore riuscito")
                            val elencoChatUtenteSelezionato = firebase.collection("ElencoChat").document(binding.emailUser.text.toString()).collection("Chat")
                            val nuovaChat = elencoChatUtenteSelezionato.document(nomeUtenteLoggato.toString())

                            nuovaChat.set(data).addOnSuccessListener {
                                var intent = Intent(this.context, SendMessageActivity::class.java)
                                intent.putExtra("nomeChat", nomeChatGenerato)
                                intent.putExtra("nomeDestinatario", binding.usernameUser.text.toString())

                                startActivity(intent)
                            }.addOnFailureListener{

                            }

                        }.addOnFailureListener{
                            Log.v("Risultato caricamento", "Caricamento nuova chat nel Firestore non riuscito")
                        }

                    }.addOnFailureListener{}

                }
            }.addOnFailureListener{
                Log.v("Caricamento Chat", "Chat non esistente")
            }
        }

    }


}