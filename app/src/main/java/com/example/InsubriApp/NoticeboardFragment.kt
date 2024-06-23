package com.example.InsubriApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.NoticeboardFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

//Fragment della Bacheca
class NoticeboardFragment : Fragment(R.layout.noticeboard_fragment) {

    private var _binding: NoticeboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        update()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NoticeboardFragmentBinding.inflate(inflater, container, false)

        update()


        val view = binding.root

        //Funzione per quando si clicca un elemento della Bacheca
        binding.listView.setOnItemClickListener{  parent, view, position, id ->
            //Metto in una variabile il post selezionato
            var selectedItem = binding.listView.adapter.getItem(position) as DocumentSnapshot

            //Creo un bundle e passo all'interno di questo tutte le informazioni del post selezionato
            var bundle = Bundle()

            if(selectedItem!=null){
                bundle.putString("Titolo", selectedItem.get("Titolo").toString())
                bundle.putString("Descrizione", selectedItem.get("Descrizione").toString())
                bundle.putString("Autore", selectedItem.get("Autore").toString())
                bundle.putDouble("Latitudine", selectedItem.get("Latitudine") as Double)
                bundle.putDouble("Longitudine", selectedItem.get("Longitudine") as Double)
                bundle.putString("Email", selectedItem.get("Email").toString())
                bundle.putString("ID", selectedItem.id)
            }

            //Setto gli argomenti da passare al SelectedPostFragment
            val fragmentPost = SelectedPostFragment()
            fragmentPost.setArguments(bundle)

            //Setto gli argomenti da passare al MapsFragment
            val fragmentMap = MapsFragment()
            fragmentMap.setArguments(bundle)

            //Transizione che porta alla schermata del post selezionat
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentPost)
                .commit()
        }

        //Collego al button la funzione addPostAction
        binding.floatingActionButton2.setOnClickListener { view->
            addPostAction(view)
        }

        return view
    }

    //Funzione per aggiornare la lista dei post
    fun update(){
        val bacheca = Firebase.firestore.collection("Bacheca")
        var arrayTitoloPost = ArrayList<DocumentSnapshot>()
        var arrayAutorePost = ArrayList<DocumentSnapshot>()
        var datiPost = HashMap<String, DocumentSnapshot>()

        //Prendo le informazioni dal Firestore da passare poi all'adapter
        bacheca.get().addOnSuccessListener { result ->
            for (item in result.documents) {
                arrayTitoloPost.add(item)
                arrayAutorePost.add(item)
                datiPost.put(item.get("Titolo").toString(), item)
            }

            //Setto l'adapter
            binding.listView.adapter = NoticeboardAdapter(requireContext(), arrayTitoloPost, arrayAutorePost)

        }
            .addOnFailureListener {
                Log.v("Firestore Bacheca", "Errore nel recupero della bacheca")
            }
    }


    //Funzione per passare alla AddPostActivity (aggiungere un post)
    fun addPostAction(view: View){
        startActivity(Intent(this.context, AddPostActivity::class.java))
    }

}