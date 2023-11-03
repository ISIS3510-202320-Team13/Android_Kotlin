package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentBookingInfoBinding

class booking_info : Fragment(){
    private var _binding: FragmentBookingInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBookingInfoBinding.inflate(inflater, container, false)
        //set up action for button

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = binding.pay
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_booking_info_to_metodoDePago2)
        }
    }

}