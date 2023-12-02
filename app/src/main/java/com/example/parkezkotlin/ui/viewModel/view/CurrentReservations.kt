package com.example.parkezkotlin.ui.viewModel.view
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentCurrentReservationsBinding
import com.example.parkezkotlin.ui.ReservationAdapter

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.parkezkotlin.ui.viewModel.ReservationViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentReservations.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentReservations : Fragment() {

    private lateinit var binding: FragmentCurrentReservationsBinding
    private val viewModel: ReservationViewModel by viewModels()
    private val reservationAdapter = ReservationAdapter()


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
                reservationAdapter.setAllReservations(reservations)
            } else {
                Snackbar.make(
                    binding.root,
                    "Error al cargar las reservas",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.fetchReservations()

    }
}