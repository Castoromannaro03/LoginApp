package com.example.InsubriApp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot

class ChatListAdapter(val context: Context, val data : ArrayList<DocumentSnapshot>) : BaseAdapter() {
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
            newView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            Log.v("conreio", " eifnoerngor")
        }

        val titolo = newView?.findViewById<TextView>(android.R.id.text1)
        titolo?.text= data[position].get("nomeChat").toString()
        Log.v("posizone e dato", titolo!!.text as String)

        return newView!!
    }
}