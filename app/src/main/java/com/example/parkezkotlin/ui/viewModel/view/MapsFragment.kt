package com.example.parkezkotlin.ui.viewModel.view

import ParkingViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.parkezkotlin.R
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null
    private val viewModel: ParkingViewModel by viewModels()
    private lateinit var cardView: CardView
    private lateinit var parkingName: TextView
    private lateinit var parkingDetails: TextView
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

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
            //map.addMarker(MarkerOptions().position(bogota).title("Marker in Bogota"))

            // Get the user's current location and add a marker there
            getCurrentLocation { location ->
                location?.let {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    map.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(bogota))
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




        googleMap?.setOnMarkerClickListener { clickedMarker ->
            // Actualizar el marcador global con el marcador que fue clickeado
            marker = clickedMarker
            val parkingId = clickedMarker.tag as? String
            if (parkingId != null) {
                viewModel.fetchParkingDetails(parkingId)
            }
            true
        }

// Escuchador para el CardView
        cardView.setOnClickListener {
            Log.d("MapsFragment", "CardView clicked")
            val parkingId = marker?.tag as? String
            if (parkingId != null) {
                val args = Bundle()
                args.putString("parking_id", parkingId)
                Log.d("MapsFragment", "Navigating to detail with ID: $parkingId")
                findNavController().navigate(R.id.action_mapsFragment_to_parkingDetail2, args)
            } else {
                Log.d("MapsFragment", "No parking ID found in marker")
            }
        }





    }
    private fun showParkingDetails(parking: parkingModel) {
        cardView.visibility = View.VISIBLE
        parkingName.text = parking.name
        parkingDetails.text = parking.availabilityCars.toString() + " cupos disponibles "+ parking.price.toString() + "$ el min"
    }



    private fun updateParkingDetailsUI(parkingDetail: parkingModel) {
        parkingDetails.text = parkingDetail.availabilityCars.toString() + " cupos disponibles "+ parkingDetail.price.toString() + " /min"
    }



    private fun addParkingMarker(location: LatLng, title: String, parkingId: String) {
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.parking)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 50, 50, false)
        val markerOptions = MarkerOptions()
            .position(location)
            .title(title)
            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))

        val marker = googleMap?.addMarker(markerOptions)
        marker?.tag = parkingId
    }

    private fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val cameraPosition = CameraPosition(
            LatLng(14.60971, 121.08175),
            15F,
            70.0F,
            0.0F

        )
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))



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
        cardView = view.findViewById(R.id.cardView)
        parkingName = view.findViewById(R.id.textView43)
        parkingDetails = view.findViewById(R.id.textView47)
        val searchButton = view.findViewById<ImageButton>(R.id.imageButton3)
        searchButton.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_searchFragment2)
        }

        val reservationButton = view.findViewById<ImageButton>(R.id.imageButton4)
        reservationButton.setOnClickListener{
            findNavController().navigate(R.id.action_mapsFragment_to_currentReservations)
        }

        backToast = Toast.makeText(context, "Presiona de nuevo para salir", Toast.LENGTH_SHORT)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        // salir de la app
                        requireActivity().finish()
                    } else {
                        backToast.show()

                        backPressedTime = System.currentTimeMillis()
                    }
                }
            })


        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            if (parkingDetail != null) {
                updateParkingDetailsUI(parkingDetail)
            }
        }

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

        val settingsButton = view.findViewById<ImageButton>(R.id.imageButton6)
        settingsButton.setOnClickListener{
            findNavController().navigate(R.id.action_mapsFragment_to_settings)
        }

        //ADD PARKINGS MARKERS
        // Inicializa el ViewModel

        // Llamar a fetchParkings
        viewModel.fetchParkings()

        viewModel.parkingsLiveData.observe(viewLifecycleOwner) { parkings ->
            parkings?.forEach { parking ->
                parking.coordinates?.let { geoPoint ->
                    val position = LatLng(geoPoint.latitude, geoPoint.longitude)
                    addParkingMarker(position, parking.name ?: "Unnamed", parking.uid ?: "")
                }
            }
        }
        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            if (parkingDetail != null) {
                showParkingDetails(parkingDetail)

            } else {
            }

        }

    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123 // You can use any request code
    }
}
