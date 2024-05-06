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
import com.google.firebase.firestore.DocumentSnapshot
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
        var datiPost = HashMap<String, DocumentSnapshot>()


        bacheca.get().addOnSuccessListener { result ->
            for (item in result.documents) {
                arrayPost.add(item.get("Titolo").toString())
                datiPost.put(item.get("Titolo").toString(), item)
            }

            //reloadListView(arrayPost)
            binding.listView.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, arrayPost)

        }
            .addOnFailureListener {
                Log.v("Firestore Bacheca", "Errore nel recupero della bacheca")
            }

        //binding.listView.adapter = this.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayPost) }


        val view = binding.root

        binding.listView.setOnItemClickListener{  parent, view, position, id ->
            var selectedItem = datiPost.get(binding.listView.getItemAtPosition(position))

            var bundle = Bundle()

            if(selectedItem!=null){
                bundle.putString("Titolo", selectedItem.get("Titolo").toString())
                bundle.putString("Descrizione", selectedItem.get("Descrizione").toString())
                bundle.putString("Autore", selectedItem.get("Autore").toString())
            }

            val fragmentPost = SelectedPostFragment()
            fragmentPost.setArguments(bundle)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentPost)
                .commit()
        }

        return view
    }

    fun reloadListView(arrayPost : ArrayList<String>){
        binding.listView.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, arrayPost)
    }

}