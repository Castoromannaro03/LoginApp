package com.example.InsubriApp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.Instant

//Activity di quando si entra nella chat con un utente
class SendMessageActivity : AppCompatActivity() {

    private val db = Firebase.database("https://insubria-app-default-rtdb.europe-west1.firebasedatabase.app")

    //Reference al ramo "Chat" del Realtime Database
    private val ref = db.getReference("Chat")

    private var arrayMessaggi = ArrayList<Message>()
    private var sortedArray = ArrayList<Message>()


    var nomeChat : String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_send_message)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        val childListener = object : ChildEventListener{
            @SuppressLint("NewApi")
            //Funzione che attende l'arrivo di un nuovo messaggio, e poi lo aggiunge
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                var messaggi = HashMap<String, Message>()
                var messaggio = Message()

                //Aggiungi alla lista di messaggi, il messaggio quando arrivi
                var mappa = arrayListOf(dataSnapshot.getValue(messaggio.javaClass))
                arrayMessaggi.add(dataSnapshot.getValue(messaggio.javaClass)!!)

                var arrayOrdinato = arrayMessaggi.sortedBy { Instant.parse(it.orario) }
                sortedArray = ArrayList(arrayOrdinato)

                //Setto l'adapter
                findViewById<ListView>(R.id.listView).adapter = MessageAdapter(baseContext, sortedArray)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                var messaggio = Message()

                arrayMessaggi.add(dataSnapshot.getValue(messaggio.javaClass)!!)

                var arrayOrdinato = arrayMessaggi.sortedBy { Instant.parse(it.orario) }
                sortedArray = ArrayList(arrayOrdinato)

                findViewById<ListView>(R.id.listView).adapter = MessageAdapter(baseContext, sortedArray)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        //Metto il titolo della chat
        var titoloChat = findViewById<TextView>(R.id.nomeChat)

        nomeChat = intent.getStringExtra("nomeChat").toString()
        val nomeDestinatario = intent.getStringExtra("nomeDestinatario")

        db.getReference("Chat/$nomeChat").addChildEventListener(childListener)

        titoloChat.text = nomeDestinatario

        var sendMessageButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        //Quando viene premuto il pulsante invio, chiamo la funzione sendMessage
        sendMessageButton.setOnClickListener{sendMessage()}

    }

    //Funzione per mandare un messaggio
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(){

        val editTextMessaggio = findViewById<TextView>(R.id.editTextMessaggio)

        var textMessaggio = editTextMessaggio.text

        editTextMessaggio.text = null

        //Creo un oggetto di tipo Message
        var messaggio = Message(textMessaggio.toString())
        messaggio.mittente=Firebase.auth.currentUser!!.email!!

        //Inserisco il messaggio nel Realtime Database
        ref.child(nomeChat).push().setValue(messaggio)
    }
}