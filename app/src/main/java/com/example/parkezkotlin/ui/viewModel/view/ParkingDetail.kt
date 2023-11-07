package com.example.parkezkotlin.ui.view

import ParkingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.data.model.Reservation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ParkingDetailFragment : Fragment() {

    private lateinit var binding: FragmentParkingDetailBinding
    private val viewModel: ParkingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        Log.d("Funciona", "Funciona")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Funciona", "Funciona")
        val parkingId = arguments?.getString("parking_id") ?: return
        Log.d("ParkingDetailFragment", "Parking ID: $parkingId")

        // Observa cambios en el LiveData de detalles de estacionamiento
        viewModel.parkingDetailLiveData.observe(viewLifecycleOwner) { parkingDetail ->
            if (parkingDetail != null) {
                updateUI(parkingDetail)
            } else {
                // Maneja el caso en el que no haya detalles disponibles
                binding.textView2.text = "Detalles no disponibles"
            }
        }

        // Solicita la data al iniciar el fragmento
        viewModel.fetchParkingDetails(parkingId)

        val reserveButton = binding.buttom
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser != null){
            reserveButton.setOnClickListener{
                val cost = 3500
                val reserveTime = binding.textTime.text.toString()
                val exitTime = System.currentTimeMillis()
                val parkId = parkingId
                val userId = currentUser.uid
                val status = "Pending"

                val reservation = Reservation(cost, reserveTime, exitTime, parkId, userId, status)

                sendReservationToFirestore(reservation)

            }
        }
    }

    private fun updateUI(parkingDetail: parkingModel) {
        binding.textView2.text = parkingDetail.name
        //"ejemplo: 5 cupos disponibles"
        binding.textView3.text = parkingDetail.availabilityCars.toString() + " cupos disponibles"
        binding.textView5.text= parkingDetail.direccion
        binding.textView11.text= parkingDetail.price.toString() + " /min"



    }

    private fun sendReservationToFirestore(reservation: Reservation) {
        val db = Firebase.firestore
        val reservationsCollection = db.collection("reservations")

        // Add the reservation data to Firestore
        reservationsCollection.add(reservation)
            .addOnSuccessListener { documentReference ->
                Log.d("Reservation", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Reservation", "Error adding document", e)
            }
    }
}
