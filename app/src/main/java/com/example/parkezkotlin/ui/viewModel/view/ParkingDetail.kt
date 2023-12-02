package com.example.parkezkotlin.ui.view

import CustomTimePickerFragment
import ParkingViewModel
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import android.Manifest
import com.example.parkezkotlin.R
import com.example.parkezkotlin.data.model.Reservation
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding
import com.example.parkezkotlin.ui.viewModel.view.MapsFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.*
import kotlin.math.abs

class ParkingDetailFragment : Fragment(), CustomTimePickerFragment.TimePickerListener {

    private lateinit var binding: FragmentParkingDetailBinding
    private val viewModel: ParkingViewModel by viewModels()
    private var valorTotal: Int = 0
    private var tarifaPorMinuto: Int = 0
    private var matchCoord: Int = 0
    private var locationPermissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        checkLocationPermissions()
        val parkingId = arguments?.getString("parking_id") ?: return



        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            if (parkingDetail != null) {
                updateUI(parkingDetail)
                getCurrentUserCoordinates()
            } else {
                binding.textView2.text = "No se encontró el parqueadero"
            }
        }

        viewModel.fetchParkingDetails(parkingId)

       /* job = scope.launch {
            while (isActive) {
                delay(6000L)

                getCurrentUserCoordinates()
            }
        }*/
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let {
            binding.buttom.setOnClickListener {
                if (binding.textTime.text.isEmpty() || binding.textTime2.text.isEmpty()) {
                    Toast.makeText(context, "Por favor, selecciona las horas de inicio y fin.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
                val fechaInicio = formato.parse(binding.textTime.text.toString())
                val fechaFin = formato.parse(binding.textTime2.text.toString())

                if (fechaInicio == null || fechaFin == null) {
                    Toast.makeText(context, "Formato de hora inválido.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (fechaFin.time - fechaInicio.time < 30 * 60000) {
                    Toast.makeText(context, "La reserva mínima es de 30 minutos.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val reserveTime = binding.textTime.text.toString() // Formato "yyyy-MM-dd HH:mm:ss"
                val exitTime = binding.textTime2.text.toString() // Formato "yyyy-MM-dd HH:mm:ss"
                val timeToReserve = valorTotal / tarifaPorMinuto // Tiempo en minutos
                val userId = currentUser.uid
                val status = "Pending"

                val reservation = Reservation(
                    cost = valorTotal.toString(),
                    entry_time = reserveTime,
                    exit_time = exitTime,
                    parking = parkingId,
                    status = status,
                    time_to_reserve = timeToReserve,
                    user = userId
                )
                sendReservationToFirestore(reservation) { reservationId ->
                    val bundle = Bundle().apply {
                        putString("parkingName", binding.textView2.text.toString())
                        putString("reservationId", reservationId)
                        putInt("totalCost", valorTotal)
                        putInt("ratePerMinute", tarifaPorMinuto)
                        putInt("timeToReserve", timeToReserve)
                    }
                    Navigation.findNavController(view).navigate(R.id.action_parkingDetail_to_booking_info, bundle)
                }
            }
        }

            binding.textTime.setOnClickListener {
            val customTimePicker = CustomTimePickerFragment().also {
                it.setOnTimeSelectedListener(this)
            }
            customTimePicker.show(parentFragmentManager, "startTimePicker")
        }

        binding.textTime2.setOnClickListener {
            val customTimePicker = CustomTimePickerFragment().also {
                it.setOnTimeSelectedListener(this)
            }
            customTimePicker.show(parentFragmentManager, "endTimePicker")
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        }
    }
    private fun getCurrentUserCoordinates() {
        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            parkingDetail?.let {
                val parkingAddress = parkingDetail.coordinates
                    getCurrentLocation { location ->
                        location?.let {
                            val userCoord = LatLng(location.latitude, location.longitude)
                            if (parkingAddress != null) {
                                val range = 50

                                val latDifference = abs(userCoord.latitude - parkingAddress.latitude)
                                val longDifference = abs(userCoord.longitude - parkingAddress.longitude)

                                if (latDifference <= range && longDifference <= range) {
                                    matchCoord++
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getCurrentLocation(callback: (LatLng?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(LatLng(location.latitude, location.longitude))
                } else {
                    Log.e(ContentValues.TAG, "Location not found")
                    callback(null)
                }
            }
        }
    }

    private fun updateUI(parkingDetail: parkingModel) {
        binding.textView2.text = parkingDetail.name
        binding.textView3.text = "${parkingDetail.availabilityCars} cupos disponibles"
        binding.textView5.text = parkingDetail.direccion
        binding.textView11.text = "${parkingDetail.price} /min"

        // Asigna directamente la tarifa por minuto si es de tipo numérico
        tarifaPorMinuto = parkingDetail.price!!.toInt()
    }

    private fun sendReservationToFirestore(reservation: Reservation, callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("reservations")
            .add(reservation)
            .addOnSuccessListener { documentReference ->
                Log.d("Reservation", "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(documentReference.id) // Pasar el ID al callback
            }
            .addOnFailureListener { e ->
                Log.e("Reservation", "Error adding document", e)
            }
    }

    override fun onTimeSelected(hourOfDay: Int, minute: Int, tag: String?) {
        val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
        if (tag == "startTimePicker") {
            binding.textTime.setText(time)
        } else if (tag == "endTimePicker") {
            binding.textTime2.setText(time)
        }

        // Solo calcular y mostrar el costo total si ambas horas están establecidas
        if (binding.textTime.text.isNotEmpty() && binding.textTime2.text.isNotEmpty()) {
            calcularYMostrarCostoTotal()
        }
    }
    private fun calcularYMostrarCostoTotal() {
        try {
            val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
            val fechaInicio = formato.parse(binding.textTime.text.toString())
            val fechaFin = formato.parse(binding.textTime2.text.toString())

            if (fechaInicio != null && fechaFin != null) {
                val duracion = (fechaFin.time - fechaInicio.time) / 60000 // Diferencia en minutos

                if (duracion >= 30) {
                    val costoTotal = duracion * tarifaPorMinuto
                    binding.textViewTotalCost.text = "Valor a pagar: $costoTotal"
                    valorTotal = costoTotal.toInt()
                } else {
                    // Mostrar un Toast indicando que la reserva mínima es de 30 minutos
                    Toast.makeText(context, "La reserva mínima es de 30 minutos.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: ParseException) {
            Log.e("ParkingDetailFragment", "Error al parsear la hora", e)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

}
