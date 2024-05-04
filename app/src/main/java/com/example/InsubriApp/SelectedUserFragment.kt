package com.example.InsubriApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.SelecteduserFragmentBinding

class SelectedUserFragment : Fragment(R.layout.selecteduser_fragment){

    private var _binding: SelecteduserFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelecteduserFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val bundle = this.arguments

        var usernameSelectedUser=""
        var emailSelectdUser=""
        var nomeSelectedUser=""
        var cognomeSelectedUser=""
        var facoltaSelectedUser =""

        if (bundle != null) {
            usernameSelectedUser = bundle.getString("Username").toString()
            emailSelectdUser = bundle.getString("Email").toString()
            nomeSelectedUser = bundle.getString("Nome").toString()
            facoltaSelectedUser = bundle.getString("Facoltà").toString()
            cognomeSelectedUser = bundle.getString("Cognome").toString()
        }

        binding.usernameUser.text = usernameSelectedUser
        binding.cognomeUser.text = cognomeSelectedUser
        binding.nomeUser.text = nomeSelectedUser
        binding.facoltaUser.text = facoltaSelectedUser
        binding.emailUser.text = emailSelectdUser

        return view
    }



}