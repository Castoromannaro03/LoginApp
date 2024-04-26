package com.example.loginapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.loginapp.databinding.NoticeboardFragmentBinding

class NoticeboardFragment : Fragment(R.layout.noticeboard_fragment) {

    private var _binding: NoticeboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo",
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo")

        _binding = NoticeboardFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.listView.adapter =
            this.context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, users) }


        return view
    }

}