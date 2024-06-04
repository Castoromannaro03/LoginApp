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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.Instant


class SendMessageActivity : AppCompatActivity() {

    private val db = Firebase.database("https://insubria-app-default-rtdb.europe-west1.firebasedatabase.app")

    private val ref = db.getReference("Chat")

    private var arrayMessaggi = ArrayList<Message>()
    private var sortedArray = ArrayList<Message>()

    var nomeChat : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_send_message)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postListener = object : ValueEventListener {
            @SuppressLint("NewApi")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var messaggi = HashMap<String, Message>()
                var messaggio = Message()
                for (snapshot : DataSnapshot in dataSnapshot.children){
                    messaggi.put(snapshot.key.toString(), snapshot.getValue(messaggio.javaClass)!!)
                }
                var list = ArrayList<Message>(messaggi.values)
                //var listaOrdinata = SortedList<messaggio.class, String>(messaggi.values)
                val list2 = list.sortedBy {it.messaggio}
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }


        }

        val childListener = object : ChildEventListener{
            @SuppressLint("NewApi")
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                var messaggi = HashMap<String, Message>()
                var messaggio = Message()
                
                /*
                for (snapshot : DataSnapshot in dataSnapshot.children){
                    messaggi.put(snapshot.key.toString(), snapshot.getValue(messaggio.javaClass)!!)
                }

                 */
                var mappa = arrayListOf(dataSnapshot.getValue(messaggio.javaClass))
                arrayMessaggi.add(dataSnapshot.getValue(messaggio.javaClass)!!)

                var arrayOrdinato = arrayMessaggi.sortedBy { Instant.parse(it.orario) }
                sortedArray = ArrayList(arrayOrdinato)

                //Toast.makeText(baseContext, dataSnapshot.getValue(messaggio.javaClass)!!.messaggio, Toast.LENGTH_SHORT).show()

                findViewById<ListView>(R.id.listView).adapter = MessageAdapter(baseContext, sortedArray)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        nomeChat = intent.getStringExtra("nomeChat").toString()
        val nomeDestinatario = intent.getStringExtra("nomeDestinatario")
        Log.v("sefrgads", nomeDestinatario!!)

        //db.getReference("Utente/Chat/Messaggio").addValueEventListener(postListener)
        db.getReference("Chat/$nomeChat").addChildEventListener(childListener)
        //db.getReference("Utente/Chat/Messaggio").removeEventListener(childListener)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(view : View){

        val editTextMessaggio = findViewById<TextView>(R.id.editTextMessaggio)

        var textMessaggio = editTextMessaggio.text

        editTextMessaggio.text = null

        var messaggio = Message(textMessaggio.toString())
        messaggio.mittente=Firebase.auth.currentUser!!.email!!

        var map = HashMap<String, Message>()

        map.put("Messaggio4", messaggio)

        //ref.child("Messaggio").setValue(mappa)
        ref.child(nomeChat).push().setValue(messaggio)
    }
}