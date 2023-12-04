package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentBookingInfoBinding
import java.text.NumberFormat

class booking_info : Fragment(){
    private var _binding: FragmentBookingInfoBinding? = null
    private val binding get() = _binding!!
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast


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

        backToast = Toast.makeText(context, "Si vuelve a ir hacia atras, cancelara su reserva actual!", Toast.LENGTH_SHORT)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    // Navegar al fragmento del mapa
                    findNavController().navigate(R.id.action_booking_info_to_searchFragment2)
                } else {
                    backToast.show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        })
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
