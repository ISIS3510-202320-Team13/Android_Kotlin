package com.example.parkezkotlin.ui.viewModel.view
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.databinding.FragmentCurrentReservationsBinding
import com.example.parkezkotlin.ui.viewModel.ReservationAdapter

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parkezkotlin.ui.viewModel.ReservationViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentReservations.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentReservations : Fragment() {

    private lateinit var binding: FragmentCurrentReservationsBinding
    private val viewModel: ReservationViewModel by viewModels()
    private var reservationAdapter = ReservationAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentReservationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = reservationAdapter

        viewModel.reservationsLiveData.observe(viewLifecycleOwner) { reservations ->
            if (reservations != null) {
                viewModel.fetchParkingLotNames()
                viewModel.getParkingLotNamesMap().observe(viewLifecycleOwner) { parkingLotNamesMap ->
                    Log.d("ParkingLotNames", parkingLotNamesMap.toString())
                    reservationAdapter = ReservationAdapter(parkingLotNamesMap)
                    reservationAdapter.setAllReservations(reservations)
                    binding.recyclerView.adapter = reservationAdapter
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Error al cargar las reservas",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val user=FirebaseAuth.getInstance().currentUser
        viewModel.fetchReservationsFromUser(user?.uid.toString())


    }
}