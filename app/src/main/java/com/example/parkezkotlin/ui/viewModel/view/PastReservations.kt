package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.databinding.FragmentPastReservationsBinding
import com.example.parkezkotlin.ui.viewModel.ReservationAdapter
import com.example.parkezkotlin.ui.viewModel.ReservationViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class PastReservations : Fragment() {
    private lateinit var binding: FragmentPastReservationsBinding
    private val viewModel: ReservationViewModel by viewModels()
    private val reservationAdapter = ReservationAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPastReservationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = reservationAdapter

        viewModel.reservationsLiveData.observe(viewLifecycleOwner) { reservations ->
            if (reservations != null) {
                reservationAdapter.setAllReservations(reservations)
            } else {
                Snackbar.make(
                    binding.root,
                    "Error al cargar las reservas",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        val userid=FirebaseAuth.getInstance().currentUser?.uid


        viewModel.fetchReservationsFromUser(userid.toString()   )

    }
}