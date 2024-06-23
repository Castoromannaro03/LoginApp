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

//Fragment dell'utente selezionato
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

        //creo un bundle
        val bundle = this.arguments

        var usernameSelectedUser=""
        var emailSelectdUser=""
        var nomeSelectedUser=""
        var cognomeSelectedUser=""
        var facoltaSelectedUser =""

        //Se il bundle è pieno, prendo tutte le informazioni
        if (bundle != null) {
            usernameSelectedUser = bundle.getString("Username").toString()
            emailSelectdUser = bundle.getString("Email").toString()
            nomeSelectedUser = bundle.getString("Nome").toString()
            facoltaSelectedUser = bundle.getString("Facoltà").toString()
            cognomeSelectedUser = bundle.getString("Cognome").toString()
        }

        //Mostro le informazioni ricevute dal bundle e cambio la UI
        binding.usernameUser.text = usernameSelectedUser
        binding.cognomeUser.text = cognomeSelectedUser
        binding.nomeUser.text = nomeSelectedUser
        binding.facoltaUser.text = facoltaSelectedUser
        binding.emailUser.text = emailSelectdUser

        return view
    }

    //Funzione chiamata quando si vuole avviare una chat con l'utente selezionato
    fun startChat(){
        val elencoChat = firebase.collection("ElencoChat").document(Firebase.auth.currentUser?.email.toString()).collection("Chat")
        //riferimento alla specifica chat con l'utente selezionato dalla ricerca
        val chatUtenteSelezionato = elencoChat.document(binding.usernameUser.text.toString())

        //Se la schermata è già pronta, avvia la chat con l'utente
        if (!binding.usernameUser.text.equals("Caricamento...")){
            chatUtenteSelezionato.get().addOnSuccessListener {
                //Se la chat già esiste, passo alla schermata della chat con l'utente selezionato, e passo l'id della chat e il nome del destinatario
                if(it.exists()){
                    var intent = Intent(this.context, SendMessageActivity::class.java)
                    intent.putExtra("nomeChat", it.get("nomeChat").toString())
                    intent.putExtra("nomeDestinatario", it.id)

                    startActivity(intent)
                } //Se la chat non esiste ancora (quindi gli utenti non si sono mai scritti)
                else{
                    var nomeUtenteLoggato = String()
                    val queryUtente = Firebase.firestore.collection("Utente").document(Firebase.auth.currentUser!!.email.toString())
                    queryUtente.get().addOnSuccessListener {
                        //Salvo in una variabile lo username dell'utente loggato
                        if(it.exists()){
                            nomeUtenteLoggato = it.get("Username").toString()
                        }

                        //Creo l'id della chat che è formato da "nome dell'utente loggato" + "-" + "nome dell'utente con cui si vuole scrivere"
                        val nomeChatGenerato = nomeUtenteLoggato + "-" + binding.usernameUser.text.toString()
                        //Creo una hashmap che contine l'id della chat
                        val data = hashMapOf(
                            "nomeChat" to nomeChatGenerato
                        )
                        //Setto i dati nel Firestore
                        //Prima salvo l'id della chat nel Firestore dell'utente destinatario
                        chatUtenteSelezionato.set(data).addOnSuccessListener {
                            val elencoChatUtenteSelezionato = firebase.collection("ElencoChat").document(binding.emailUser.text.toString()).collection("Chat")
                            val nuovaChat = elencoChatUtenteSelezionato.document(nomeUtenteLoggato.toString())

                            //Poi salvo l'id della chat nel Firestore dell'utente loggato, e poi passo alla schermata della chat, passando l'id della chat e il nome del destinatario
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