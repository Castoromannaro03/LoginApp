package com.example.InsubriApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FacultyViewModel: ViewModel() {

    private val _itemList = MutableLiveData<MutableList<Faculty.Item>>(mutableListOf(
    Faculty.Item("Biotecnologie", true),
    Faculty.Item("Chimica e chimica industriale", false),
    Faculty.Item("Economia e management", false),
    Faculty.Item("Educazione professionale", false),
    Faculty.Item("Economia e management", false),
    Faculty.Item("Educazione professionale", false),
    Faculty.Item("Fisica", false),
    Faculty.Item("Fisioterapia", false),
    Faculty.Item("Igiene dentale", false),
    Faculty.Item("Infermieristica", false),
    Faculty.Item("Informatica (la migliore)", false),
    Faculty.Item("Ingegneria per la sicurezza", false),
    Faculty.Item("Matematica", false),
    Faculty.Item("Ostetrica", false),
    Faculty.Item("Scienze biologiche", false),
    Faculty.Item("Scienze del turismo", false),
    Faculty.Item("Scienze dell'ambiente e della natura", false),
    Faculty.Item("Scienze della comunicazione", false),
    Faculty.Item("Scienze della mediazione", false),
    Faculty.Item("Scienze motorie", false),
    Faculty.Item("Storia del mondo contemporaneo", false),
    Faculty.Item("Tecniche della prevenzione", false),
    Faculty.Item("Tecniche di laboratorio biomedico", false),
    Faculty.Item("Tecniche di radiologia medica", false),
    ))

    val itemList: LiveData<MutableList<Faculty.Item>> get() = _itemList

    private val _checkedItems = MutableLiveData<List<String>>()
    val checkedItems: LiveData<List<String>> get() = _checkedItems

    fun updateItem(index: Int, isChecked: Boolean) {
        _itemList.value?.let {
            if (index >= 0 && index < it.size) {
                it[index].isChecked = isChecked
                _itemList.value = it
                updateCheckedItems()
            }
        }
    }

    private fun updateCheckedItems() {
        _checkedItems.value = _itemList.value?.filter { it.isChecked }?.map { it.text } ?: emptyList()
    }

    fun getCheckedItems(): List<String> {
        return _checkedItems.value ?: emptyList()
    }

    public fun modifica() {

        updateItem(0, false)

    }

}