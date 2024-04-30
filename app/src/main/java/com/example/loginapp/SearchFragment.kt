package com.example.loginapp

import android.R.layout
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginapp.databinding.SearchFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
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
                        var hashMap = HashMap<String, DocumentSnapshot>()
                        for(i in result.documents){
                            if(i.get("Username").toString().lowercase().contains(binding.SearchTextView.text.toString().lowercase())){
                                hashMap.put(i.get("Username").toString(), i)
                                arrayList.add(i.get("Username").toString())
                            }
                        }

                        //binding.listView.adapter = thisFragment.context?.let { SimpleAdapter(thisFragment.context , arrayList, android.R.layout.simple_list_item_2, arrayOf("Username", "Email"), intArrayOf(android.R.id.text1, android.R.id.text2) )}
                        //binding.listView.adapter = thisFragment.context?.let { SimpleAdapter(thisFragment.context , arrayList, android.R.layout.simple_list_item_2, arrayOf("Username", "Email"), intArrayOf(android.R.id.text1, android.R.id.text2) )}
                        binding.listView.adapter = thisFragment.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, arrayList.toArray()) }

                        binding.listView.setOnItemClickListener { parent, view, position, id ->
                            if(binding.SearchTextView.text.length>=3){
                                //Toast.makeText(thisFragment.context,binding.listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                                var selectedItem = hashMap.get(binding.listView.getItemAtPosition(position).toString())



                                val bundle = Bundle()

                                bundle.putString("Username", binding.listView.getItemAtPosition(position).toString())
                                bundle.putString("Nome", selectedItem?.get("Nome").toString())
                                bundle.putString("Cognome", selectedItem?.get("Cognome").toString())
                                bundle.putString("Email", selectedItem?.id.toString())

                                val fragment2 = SelectedUserFragment()
                                fragment2.setArguments(bundle)

                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer, fragment2)
                                    .commit()
                            }

                        }

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