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

class MapsFragment : Fragment() {

    val FINE_PERMISSION_CODE = 1
    var currentLocation : Location = Location("Sydney")
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient



    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        val bundleForMaps = this.arguments

        var latitudine = currentLocation.latitude
        var longitudine = currentLocation.longitude

        Log.v("Bundle", bundleForMaps?.size().toString())

        if(bundleForMaps!=null) {

            latitudine = bundleForMaps.getDouble("Latitudine")
            longitudine = bundleForMaps.getDouble("Longitudine")

        }


        val sydney = LatLng(latitudine, longitudine)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getLastLocation()

        return inflater.inflate(R.layout.maps_fragment, container, false)
    }

    fun getLastLocation() {

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

            if(location != null) {

                currentLocation = location

                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)

            }
            else{
                //Toast.makeText(requireContext(), "location nulla", Toast.LENGTH_LONG).show()
            }
            Log.v("Posizione", currentLocation.longitude.toString() + " " + currentLocation.latitude.toString())


        }.addOnFailureListener{
            /*
            currentLocation.latitude = 0.0
            currentLocation.longitude = 0.0
             */
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_PERMISSION_CODE) {

            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation()

            } else {

                Toast.makeText(this.context, "Permessi negati", Toast.LENGTH_SHORT).show()

            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = OnMapReadyCallback { googleMap ->
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            val sydney = LatLng(-34.0, 151.0)
            googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f))
        }

    }


}