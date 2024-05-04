package com.example.InsubriApp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.NoticeboardFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NoticeboardFragment : Fragment(R.layout.noticeboard_fragment) {

    private var _binding: NoticeboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoticeboardFragmentBinding.inflate(inflater, container, false)


        val bacheca = Firebase.firestore.collection("Bacheca")
        var arrayPost = ArrayList<String>()

        arrayPost.add("Ciao")


        bacheca.get().addOnSuccessListener { result ->
            for (item in result.documents) {
                Log.v("ERRORONE", item.get("Titolo").toString())
                arrayPost.add(item.get("Titolo").toString())
            }






        }
            .addOnFailureListener {
                Log.v("Firestore Bacheca", "Errore nel recupero della bacheca")
            }

        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo",
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo"
        )

        binding.listView.adapter =
            this.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayPost) }


        val view = binding.root

        return view
    }

}