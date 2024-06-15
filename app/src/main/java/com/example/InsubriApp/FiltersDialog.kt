package com.example.InsubriApp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.InsubriApp.databinding.UserfiltersDialogBinding

class FiltersDialog : DialogFragment() {

    private lateinit var listView: ListView
    private lateinit var itemAdapter: FacultyAdapter
    private lateinit var itemList: MutableList<Faculty.Item>
    private val facultyViewModel: FacultyViewModel by activityViewModels()
    private var _binding: UserfiltersDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = UserfiltersDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        return inflater.inflate(R.layout.userfilters_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listaFacolta)



        itemAdapter = FacultyAdapter(requireContext(), facultyViewModel.itemList.value!!) { position, isChecked ->
            facultyViewModel.updateItem(position, isChecked)
        }
        listView.adapter = itemAdapter


    }

}