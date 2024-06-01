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

        //binding.chatList.adapter=chatListAdapter


        val elencoChat = firebase.collection("ElencoChat").document(Firebase.auth.currentUser?.email.toString()).collection("Chat")
        //val elencoChat = firebase.collection("ElencoChat")
        //val query = elencoChat.whereEqualTo(FieldPath.documentId(), Firebase.auth.currentUser?.email.toString())
        //val query = elencoChat.get()
        elencoChat.get().addOnSuccessListener {
            if(it.documents.size>0){
                for(document in it.documents){
                    querySnapshotList.add(document)
                    Log.v("Errore Query", document.get(FieldPath.documentId()).toString())
                }
            }
            chatListAdapter = ChatListAdapter(this.requireContext(), querySnapshotList)

            binding.chatList.adapter=chatListAdapter
            Log.v("Errore Query", "Query elenco Chat riuscita")
        }.addOnFailureListener{
            Log.v("Errore Query", "Query elenco Chat non riuscita")
        }



        binding.chatList.setOnItemClickListener{ parent, view, position, id ->
            var selectedItem = binding.chatList.adapter.getItem(position) as DocumentSnapshot
            Log.v("Chat selezionata",selectedItem.get("nomeChat").toString())


            var intent = Intent(this.context, SendMessageActivity::class.java)
            intent.putExtra("nomeChat", selectedItem.get("nomeChat").toString())
            intent.putExtra("nomeDestinatario", selectedItem.get(FieldPath.documentId()).toString())

            startActivity(intent)
        }
        val view = binding.root
        return view
    }
}