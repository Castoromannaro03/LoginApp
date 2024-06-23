package com.example.InsubriApp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox

//Classe che gestisce la visualizzazione e selezione degli elementi delle liste
class FacultyAdapter(val context: Context, private val itemList: List<Faculty.Item>, private val onCheckedChange: (Int, Boolean) -> Unit) : BaseAdapter() {


    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        var viewHolder: ViewHolder

        //Se non esiste una view, crea la visualizzazione
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.faculty_checkbox, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        //Salvo in delle variabili gli elementi della checkbox
        val item = itemList[position]
        viewHolder.checkBox.text = item.text
        viewHolder.checkBox.isChecked = item.isChecked
        //Funzione che fa cambiare lo stato di una checkbox
        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(position, isChecked)
        }

        return view
    }

    //Classe che rappresenta l'oggetto checkbox
    private class ViewHolder(view: View) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }
}