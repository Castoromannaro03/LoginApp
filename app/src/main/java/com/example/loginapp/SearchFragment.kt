package com.example.loginapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.loginapp.databinding.SearchFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SearchFragment : Fragment(R.layout.search_fragment) {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val thisFragment = this

        val lessThan3LettersMessage = arrayOf(
            "Inserisci almeno 3 caratteri per iniziare la ricerca"
        )

        binding.listView.adapter = thisFragment.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, lessThan3LettersMessage) }

        binding.SearchTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            /*DA SISTEMARE
            *
            *
            *
            *
            *
             */
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(binding.SearchTextView.text.length>=3){
                    var queryRicerca = Firebase.firestore.collection("Utente")
                    queryRicerca.get().addOnSuccessListener { result->
                        var arrayList = arrayListOf<String>()
                        for(item in result.documents){
                            if(item.get("Username").toString().lowercase().contains(binding.SearchTextView.text.toString().lowercase())){
                                arrayList.add(item.get("Username").toString())
                            }
                        }
                        binding.listView.adapter = thisFragment.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayList.toArray()) }
                    }

                }
                else{
                    binding.listView.adapter = thisFragment.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, lessThan3LettersMessage) }
                }
            }
        })

        return view
    }

}