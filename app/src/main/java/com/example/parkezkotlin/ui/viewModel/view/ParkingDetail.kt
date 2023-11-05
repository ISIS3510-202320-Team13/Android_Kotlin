package com.example.parkezkotlin.ui.view

import ParkingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding
import com.example.parkezkotlin.data.model.parkingModel
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
    }

    private fun updateUI(parkingDetail: parkingModel) {
        binding.textView2.text = parkingDetail.name
    }
}
