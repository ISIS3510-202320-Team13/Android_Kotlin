package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentMetodoDePagoBinding

class MetodoDePago : Fragment() {

    private var _binding: FragmentMetodoDePagoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMetodoDePagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val payButton = binding.button10
        payButton.setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.action_metodoDePago2_to_recibos)
        }
        super.onViewCreated(view, savedInstanceState)
    }


}