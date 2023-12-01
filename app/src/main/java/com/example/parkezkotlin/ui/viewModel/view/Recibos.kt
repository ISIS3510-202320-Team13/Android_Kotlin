package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentRecibosBinding

class Recibos : Fragment() {
    private var _binding: FragmentRecibosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRecibosBinding.inflate(inflater, container, false)

        // Recibiendo los argumentos del bundle y asignándolos a los TextViews
        val parkingName = arguments?.getString("parkingName") ?: "Nombre no disponible"
        val reservationId = arguments?.getString("reservationId") ?: "ID no disponible"
        val totalCost = arguments?.getInt("totalCost") ?: 0
        val timeToReserve= arguments?.getInt("timeToReserve") ?: 0

        binding.editTextText10.text = parkingName
        binding.editTextText11.text = reservationId
        binding.editTextText7.text = "$timeToReserve min - $${totalCost}"

        val button = binding.button
        button.setOnClickListener {
            findNavController().navigate(R.id.action_recibos_to_mapsFragment)


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Recibos().apply {
                arguments = Bundle().apply {
                    // Aquí se pueden establecer los argumentos si es necesario
                }
            }
    }
}
