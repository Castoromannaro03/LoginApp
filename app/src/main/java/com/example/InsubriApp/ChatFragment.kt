package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.ChatFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore

//Fragment contenente la lista delle chat
class ChatFragment : Fragment(R.layout.chat_fragment) {
    //binding
    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!

    var firebase = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChatFragmentBinding.inflate(inflater, container, false)

        var querySnapshotList = ArrayList<DocumentSnapshot>()

        var chatListAdapter = ChatListAdapter(this.requireContext(), querySnapshotList)


        val elencoChat = firebase.collection("ElencoChat").document(Firebase.auth.currentUser?.email.toString()).collection("Chat")

        //Aggiungo ad una lista i dati delle chat dell'utente
        elencoChat.get().addOnSuccessListener {
            if(it.documents.size>0){
                for(document in it.documents){
                    querySnapshotList.add(document)
                }
            }

            //Setto l'adapter
            chatListAdapter = ChatListAdapter(this.requireContext(), querySnapshotList)

            binding.chatList.adapter=chatListAdapter
        }.addOnFailureListener{
            Log.v("Errore Query", "Query elenco Chat non riuscita")
        }


        //In base all'utente che seleziono nella lista delle chat, entro nella sua chat
        binding.chatList.setOnItemClickListener{ parent, view, position, id ->
            var selectedItem = binding.chatList.adapter.getItem(position) as DocumentSnapshot
            Log.v("Chat selezionata",selectedItem.get("nomeChat").toString())

            //Passo all'activity per mandare il messaggio il nome del destinatario e l'id della chat, e poi passo al SendMessageActivity
            var intent = Intent(this.context, SendMessageActivity::class.java)
            intent.putExtra("nomeChat", selectedItem.get("nomeChat").toString())
            intent.putExtra("nomeDestinatario", selectedItem.id)

            startActivity(intent)
        }
        val view = binding.root
        return view
    }
}