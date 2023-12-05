package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentRecibosBinding

class Recibos : Fragment() {
    private var _binding: FragmentRecibosBinding? = null
    private val binding get() = _binding!!
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRecibosBinding.inflate(inflater, container, false)

        // Recibiendo los argumentos del bundle y asignándolos a los TextViews
        val parkingName = arguments?.getString("parkingName") ?: "Nombre no disponible"
        val reservationId = arguments?.getString("reservationId") ?: "ID no disponible"
        val totalCost = arguments?.getInt("totalCost") ?: 0
        val timeToReserve = arguments?.getInt("timeToReserve") ?: 0
        val date = arguments?.getLong("date") ?: 0

        binding.editTextText10.text = parkingName
        binding.editTextText11.text = reservationId
        binding.editTextText7.text = "$timeToReserve min - $${totalCost}"




        val button = binding.button
        button.setOnClickListener {
            findNavController().navigate(R.id.action_recibos_to_mapsFragment)


        }



        backToast = Toast.makeText(context, "Presiona de nuevo para ir al mapa", Toast.LENGTH_SHORT)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        // Navegar al fragmento del mapa
                        findNavController().navigate(R.id.action_recibos_to_mapsFragment)
                    } else {
                        backToast.show()
                        backPressedTime = System.currentTimeMillis()
                    }
                }
            })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}