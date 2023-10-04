package com.example.parkezkotlin

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.parkezkotlin.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

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
        // access to the user location


        val bogota = LatLng(4.60971, -74.08175)
        googleMap.addMarker(MarkerOptions().position(bogota).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bogota))
        //zoom ti the marker position

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bogota, 12f))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        /*val button = binding.imageButton5
        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_mapsFragment_to_booking_info)
        }
        val button2 = binding.imageButton4
        button2.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_mapsFragment_to_parkingDetail)
        }


        button2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_main_to_signUpFragment2)
        }
        */


        //load and apply custom style
        try {
            val success = mapFragment?.getMapAsync { googleMap ->
                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
                googleMap.setMapStyle(mapStyleOptions)
            }
            if (success == null) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsFragment", "Raw resource for map style not found: $e")
        }


    }
}





