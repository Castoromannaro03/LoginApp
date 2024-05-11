package com.example.InsubriApp

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.InsubriApp.databinding.SelectedpostFragmentBinding

class SelectedPostFragment : Fragment(R.layout.selectedpost_fragment) {

    private var _binding: SelectedpostFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectedpostFragmentBinding.inflate(inflater, container,false)

        val bundle = this.arguments

        var latitudine = 0.0
        var longitudine = 0.0

        if(bundle!=null){
            binding.textTitolo.text=bundle.getString("Titolo")
            binding.textAutore.text=bundle.getString("Autore")
            binding.textDescrizione.text=bundle.getString("Descrizione")
            latitudine = bundle.getDouble("Latitudine")
            longitudine = bundle.getDouble("Longitudine")
        }

        var bundleForMaps = Bundle()

        bundleForMaps.putDouble("Latitudine", latitudine)
        bundleForMaps.putDouble("Longitudine", longitudine)

        val fragmentMap = MapsFragment()
        fragmentMap.setArguments(bundleForMaps)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentMap)
            .commit()

        binding.textAutore.paintFlags = binding.textAutore.paintFlags or Paint.UNDERLINE_TEXT_FLAG



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    Log.d("backPressed", "Fragment back pressed invoked")
                    // Do custom work here
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, NoticeboardFragment())
                        .commit()

                }
            }
        )

        val view = binding.root
        return view
    }

}

