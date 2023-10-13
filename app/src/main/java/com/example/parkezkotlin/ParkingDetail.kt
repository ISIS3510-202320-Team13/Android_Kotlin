package com.example.parkezkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding

class ParkingDetail : Fragment() {

    private var _binding: FragmentParkingDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentParkingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val automaticReservation = binding.buttom
        automaticReservation.setOnClickListener {
            // go to the payment fragment
            Navigation.findNavController(view).navigate(R.id.action_parkingDetail_to_booking_info)
        }
    }

}
