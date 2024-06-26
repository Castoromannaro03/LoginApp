package com.example.InsubriApp

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//Classe che gestisce la visualizzazione dei messaggi
class MessageAdapter(val context: Context, var data : ArrayList<Message>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return (position).toLong()
    }

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var newView = convertView
        /*
        if (newView == null) {
            newView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        }
         */


        var orario = Timestamp(Instant.parse(data[position].orario)).toDate()


        val PATTERN_FORMAT = "d MMMM YYYY - HH:mm";
        val formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
            .withZone(ZoneId.systemDefault())

        val formattedInstant = formatter.format(Instant.parse(data[position].orario))

        //testo?.text = orario.toString()


        if(data[position].mittente!= Firebase.auth.currentUser!!.email){

            newView = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)

            newView.findViewById<TextView>(R.id.textDateTime).text = formattedInstant.toString()
            newView.findViewById<TextView>(R.id.textMessage).text = data[position].messaggio
        }else{

            newView = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            val messaggio = newView?.findViewById<TextView>(R.id.textMessage)
            messaggio?.text = data[position].messaggio
            val orario = newView?.findViewById<TextView>(R.id.textDateTime)
            orario?.text = formattedInstant.toString()
            //newView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, true)
            //titolo!!.gravity= Gravity.END
            //testo!!.gravity= Gravity.END
        }
        /*
        if(data[position].mittente!=Firebase.auth.currentUser!!.email){
            titolo!!.gravity=Gravity.END
            testo!!.gravity=Gravity.END
        }
        else{
            titolo!!.gravity=Gravity.START
            testo!!.gravity=Gravity.START
        }

         */

        return newView!!
    }
}