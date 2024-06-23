package com.example.InsubriApp

import android.R.layout
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.SearchFragmentBinding
import com.example.InsubriApp.databinding.UserfiltersDialogBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

//Fragment per la ricerca utenti
class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var facultyAdapter: FacultyAdapter
    //Per il "pop-up" dei filtri
    private val facultyViewModel: FacultyViewModel by activityViewModels()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        facultyViewModel.modifica()

        val thisFragment = this


        val lessThan3LettersMessage = arrayOf(
            "Inserisci almeno 3 caratteri per iniziare la ricerca"
        )


        //Quando viene il testo inserito viene cambiato, aggiorno la UI
        binding.SearchTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


                    //Controllo se l'utente ha inserito almeno 3 caratteri
                    if (binding.SearchTextView.text.length >= 3) {

                        var queryRicerca = Firebase.firestore.collection("Utente")

                        //Aggiorna la lista degli utenti mostrati se viene aggiornato qualcosa nel pop-up delle facoltà
                        facultyViewModel.checkedItems.observe(viewLifecycleOwner, Observer { checkedItems ->

                        //Controllo se l'utente ha inserito almeno 3 caratteri e se la lista di filtri è vuota
                        if (checkedItems.isEmpty() && binding.SearchTextView.text.length >= 3) {

                            queryRicerca.get().addOnSuccessListener { result ->

                                //Salvo i dati nell'hashmap per poi passarli al bundle
                                var hashMap = HashMap<String, DocumentSnapshot>()
                                //Salvo i dati in una ArrayList che poi passerò all'Adapter
                                var arraySnapshot = arrayListOf<DocumentSnapshot>()

                                //Scorro tutti i documents
                                for (i in result.documents) {
                                    //Controllo se la stringa inserita dall'utente corrisponde allo username dell'utente che stiamo analizzando
                                    if (i.get("Username").toString().lowercase().contains(binding.SearchTextView.text.toString().lowercase())) {
                                        hashMap.put(i.get("Username").toString(), i)
                                        arraySnapshot.add(i)
                                    }
                                }

                                //Setto l'adapter
                                binding.listView.adapter = SearchAdapter(requireContext(), arraySnapshot)

                                //Quando viene selezionato un utente, passo al Fragment dell'utente selezionato passandone i dati
                                binding.listView.setOnItemClickListener { parent, view, position, id ->
                                    if (binding.SearchTextView.text.length >= 3) {

                                        var item = binding.listView.adapter.getItem(position) as DocumentSnapshot
                                        var selectedItem = hashMap.get(item.get("Username"))
                                        val bundle = Bundle()

                                        bundle.putString("Username", selectedItem?.get("Username").toString())
                                        bundle.putString("Nome", selectedItem?.get("Nome").toString())
                                        bundle.putString("Cognome", selectedItem?.get("Cognome").toString())
                                        bundle.putString("Facoltà", selectedItem?.get("Facoltà").toString())
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
                        //Ricerca dell'utente se c'è qualche filtro selezionato
                        else {

                            //Controllo se l'utente ha inserito almeno 3 caratteri
                            if(binding.SearchTextView.text.length >= 3) {

                                //Perché il Firestore con il whereIn non può gestire più di 10 valori
                                val batchSize = 10
                                //Inserisco la lista delle facoltà in questa variabile in modo da "dividere" la lista
                                val batches = checkedItems.chunked(batchSize)

                                batches.forEach { batch ->

                                    //Effettuo la ricerca degli utenti inserendo come filtro una lista delle facoltà che ha selezionato l'utente
                                    queryRicerca.whereIn("Facoltà", batch).get().addOnSuccessListener { result ->
                                        //Salvo i dati nell'hashmap per poi passarli al bundle
                                        var hashMap = HashMap<String, DocumentSnapshot>()
                                        //Salvo i dati in una ArrayList che poi passerò all'Adapter
                                        var arraySnapshot = arrayListOf<DocumentSnapshot>()

                                        //Scorro tutti i documents
                                        for (i in result.documents) {
                                            //Controllo se la stringa inserita dall'utente corrisponde allo username dell'utente che stiamo analizzando
                                            if (i.get("Username").toString().lowercase().contains(binding.SearchTextView.text.toString().lowercase())) {
                                                    hashMap.put(i.get("Username").toString(), i)
                                                    arraySnapshot.add(i)
                                                }
                                            }

                                            //Setto l'adapter
                                            binding.listView.adapter = SearchAdapter(requireContext(), arraySnapshot)

                                        //Quando viene selezionato un utente, passo al Fragment dell'utente selezionato passandone i dati
                                        binding.listView.setOnItemClickListener { parent, view, position, id ->
                                                if (binding.SearchTextView.text.length >= 3) {
                                                    var item = binding.listView.adapter.getItem(position) as DocumentSnapshot
                                                    var selectedItem = hashMap.get(item.get("Username"))
                                                    val bundle = Bundle()

                                                    bundle.putString("Username", selectedItem?.get("Username").toString())
                                                    bundle.putString("Nome", selectedItem?.get("Nome").toString())
                                                    bundle.putString("Cognome", selectedItem?.get("Cognome").toString())
                                                    bundle.putString("Facoltà", selectedItem?.get("Facoltà").toString())
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

                            }

                        }

                        })


                    } else {
                        binding.listView.adapter = null
                    }


            }

        })





        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Chiamo la funzione
        val button: ImageButton = view.findViewById(R.id.imageButton3)
        button.setOnClickListener {
            showDialog()
        }

    }

    //Funzione per mostrare il pop-up dei filtri
    private fun showDialog() {
        val fragmentManager = parentFragmentManager
        val newFragment = FiltersDialog()
        newFragment.show(fragmentManager, "dialog")
    }

}