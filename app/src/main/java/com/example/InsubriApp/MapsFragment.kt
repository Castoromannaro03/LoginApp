package com.example.InsubriApp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//Fragment contenente la mappa
class MapsFragment : Fragment() {

    val FINE_PERMISSION_CODE = 1
    var currentLocation : Location = Location("Sydney")
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient



    //Variabile che viene chiamata quando la mappa è pronta
    private val callback = OnMapReadyCallback { googleMap ->

        //Creo un bundle
        val bundleForMaps = this.arguments

        var latitudine = currentLocation.latitude
        var longitudine = currentLocation.longitude

        //Prendo le informazioni dal bundle
        if(bundleForMaps!=null) {

            latitudine = bundleForMaps.getDouble("Latitudine")
            longitudine = bundleForMaps.getDouble("Longitudine")
        }

        //Creo una variabile che contiene la latitudine e longitudine
        val sydney = LatLng(latitudine, longitudine)
        //Mostro la posizione sulla mappa
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Prendo il provider della posizione e poi chiamo la funzione che prende la posizione
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getLastLocation()

        return inflater.inflate(R.layout.maps_fragment, container, false)
    }

    //Funzione per ottenere l'ultima posizione
    fun getLastLocation() {

        //Controllo i permessi per accedere alla posizione
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        } else {

        }

        var task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {location ->

            //Se trova la posizione, la salva in una variabile
            if(location != null) {

                currentLocation = location

                //Selezione il map fragment e chiamando la funzione getMapAsync mostro il marker sulla mappa
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)

            }


        }.addOnFailureListener{

        }
    }

    //Funzione che analizza i permessi ottenuti
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_PERMISSION_CODE) {

            //Se i permessi ci sono, chiamo la funzione che mostra la posizione dell'utente
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation()

            } //Altrimenti mostra messaggio di errore
            else {

                Toast.makeText(this.context, "Permessi negati", Toast.LENGTH_SHORT).show()

            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}