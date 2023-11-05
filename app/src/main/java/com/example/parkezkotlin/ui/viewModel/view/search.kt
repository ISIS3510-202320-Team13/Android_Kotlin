package com.example.parkezkotlin.ui.view

import ParkingViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentSearch2Binding
import com.example.parkezkotlin.ui.ParkingAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearch2Binding
    private val viewModel: ParkingViewModel by viewModels()
    private val parkingAdapter = ParkingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearch2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el LayoutManager para el RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Configura el adaptador para el RecyclerView
        binding.recyclerView.adapter = parkingAdapter

        // Observa cambios en el LiveData
        viewModel.parkingsLiveData.observe(viewLifecycleOwner, { parkings ->
            if (parkings != null) {
                parkingAdapter.submitList(parkings)
            } else {
                // Maneja el error (mostrar un mensaje, etc.)
            }
        })

        // Solicita la data al iniciar el fragmento
        viewModel.fetchParkings()
    }
}
