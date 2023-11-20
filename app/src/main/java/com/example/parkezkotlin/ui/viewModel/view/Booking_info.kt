package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentBookingInfoBinding
import java.text.NumberFormat

class booking_info : Fragment(){
    private var _binding: FragmentBookingInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera la información pasada desde el fragmento anterior
        val parkingName = arguments?.getString("parkingName") ?: "Nombre no disponible"
        val reservationId = arguments?.getString("reservationId") ?: "ID no disponible"
        val totalCost = arguments?.getInt("totalCost") ?: 0
        val timeToReserve= arguments?.getInt("timeToReserve") ?: 0


        // Actualiza la UI con los datos recuperados
        binding.editTextText.text = "Reserva en $parkingName"

        binding.editTextText3.text = "ID de reserva: $reservationId"
        binding.textViewTotalCost.text ="Total a pagar: ${totalCost.formatAsCurrency()}"

        binding.pay.setOnClickListener {
            val bundle = Bundle().apply {
                putString("parkingName", parkingName)
                putString("reservationId", reservationId)
                putInt("totalCost", totalCost)
                putInt("timeToReserve", timeToReserve)
            }
            Navigation.findNavController(view).navigate(R.id.action_booking_info_to_metodoDePago2, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Función de extensión para formatear el costo como moneda
    private fun Int.formatAsCurrency(): String {
        return NumberFormat.getCurrencyInstance().format(this)
    }
}
