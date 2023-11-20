package com.example.parkezkotlin.ui.view

import CustomTimePickerFragment
import ParkingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.data.model.Reservation
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ParkingDetailFragment : Fragment(), CustomTimePickerFragment.TimePickerListener {

    private lateinit var binding: FragmentParkingDetailBinding
    private val viewModel: ParkingViewModel by viewModels()
    private var valorTotal: Int = 0
    private var tarifaPorMinuto: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parkingId = arguments?.getString("parking_id") ?: return

        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            if (parkingDetail != null) {
                updateUI(parkingDetail)
            } else {
                binding.textView2.text = "No se encontró el parqueadero"
            }
        }

        viewModel.fetchParkingDetails(parkingId)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let {
            binding.buttom.setOnClickListener {
                val cost = "3500"
                val reserveTime = binding.textTime.text.toString()
                val exitTime = System.currentTimeMillis()
                val userId = currentUser.uid
                val status = "Pending"

                val reservation = Reservation(cost, reserveTime, exitTime, parkingId, status, userId)
                // sendReservationToFirestore(reservation)
                Navigation.findNavController(view).navigate(R.id.action_parkingDetail_to_booking_info)
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

    private fun updateUI(parkingDetail: parkingModel) {
        binding.textView2.text = parkingDetail.name
        binding.textView3.text = "${parkingDetail.availabilityCars} cupos disponibles"
        binding.textView5.text = parkingDetail.direccion
        binding.textView11.text = "${parkingDetail.price} /min"

        // Asigna directamente la tarifa por minuto si es de tipo numérico
        tarifaPorMinuto = parkingDetail.price!!
    }

    private fun sendReservationToFirestore(reservation: Reservation) {
        val db = FirebaseFirestore.getInstance()
        db.collection("reservations")
            .add(reservation)
            .addOnSuccessListener { documentReference ->
                Log.d("Reservation", "DocumentSnapshot added with ID: ${documentReference.id}")
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


}
