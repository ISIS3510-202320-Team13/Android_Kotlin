package com.example.parkezkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.parkezkotlin.databinding.FragmentMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are granted; enable location features
            map.isMyLocationEnabled = true

            val bogota = LatLng(4.60971, -74.08175)
            map.addMarker(MarkerOptions().position(bogota).title("Marker in Bogota"))

            // Get the user's current location and add a marker there
            getCurrentLocation { location ->
                location?.let {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    map.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
                }
            }
        } else {
            // Permissions are not granted; request location permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }

        // Set a click listener to add a marker where the user taps
        map.setOnMapClickListener { latLng ->
            addMarker(latLng)
        }
    }

    private fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(LatLng(location.latitude, location.longitude))
                } else {
                    Log.e(TAG, "Location not found")
                    callback(null)
                }
            }
        }
    }

    private fun addMarker(latLng: LatLng) {
        // Remove the previous marker, if it exists
        marker?.remove()

        // Add a new marker at the specified LatLng position
        marker = googleMap?.addMarker(MarkerOptions().position(latLng).title("User Added Marker"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Load and apply custom map style
        try {
            val success = mapFragment?.getMapAsync { map ->
                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
                map.setMapStyle(mapStyleOptions)
            }
            if (success == null) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsFragment", "Raw resource for map style not found: $e")
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123 // You can use any request code
    }
}
