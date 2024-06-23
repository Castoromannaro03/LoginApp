package com.example.InsubriApp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot

//Classe che gestisce la visualizzazione dei post della bacheca
class NoticeboardAdapter  (val context: Context, val data : ArrayList<DocumentSnapshot>, val data1 : ArrayList<DocumentSnapshot>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var newView = convertView
        if (newView == null) {
            newView = LayoutInflater.from(context).inflate(R.layout.post, parent, false)
        }

        val titolo = newView?.findViewById<TextView>(R.id.titoloPost)
        val autore = newView?.findViewById<TextView>(R.id.autorePost)
        titolo?.text= data[position].get("Titolo").toString()
        autore?.text = buildString {
            append("post di ")
            append(data[position].get("Autore").toString())
        }

        return newView!!
    }
}