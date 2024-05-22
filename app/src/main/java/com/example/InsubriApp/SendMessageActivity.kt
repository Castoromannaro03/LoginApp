package com.example.InsubriApp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class SendMessageActivity : AppCompatActivity() {

    private val db = Firebase.database("https://insubria-app-default-rtdb.europe-west1.firebasedatabase.app")

    private val ref = db.getReference("Utente")
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
                var listTesto = ArrayList<String>()

                for(temp: Message in list2){
                    listTesto.add(temp.messaggio!!)
                }

                findViewById<TextView>(R.id.post).text= listTesto.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }


        }

        db.getReference("Utente/Chat/Messaggio").addValueEventListener(postListener)


    }

    fun sendMessage(view : View){

        val editTextMessaggio = findViewById<TextView>(R.id.editTextMessaggio)

        var textMessaggio = editTextMessaggio.text

        var messaggio = Message(textMessaggio.toString())

        var map = HashMap<String, Message>()

        map.put("Messaggio4", messaggio)

        //ref.child("Messaggio").setValue(mappa)
        ref.child("Chat/Messaggio").push().setValue(messaggio)
    }
}