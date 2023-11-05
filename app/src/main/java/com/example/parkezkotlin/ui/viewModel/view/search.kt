package com.example.parkezkotlin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentSearch2Binding
import com.example.parkezkotlin.ui.ParkingAdapter
import ParkingViewModel
import androidx.appcompat.widget.SearchView
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
                parkingAdapter.setAllParkings(parkings)
            } else {
                // Maneja el error (mostrar un mensaje, etc.)
            }
        })

        // Solicita la data al iniciar el fragmento
        viewModel.fetchParkings()

        // Configurar el SearchView
        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                parkingAdapter.filter(newText ?: "")
                return true
            }
        })

    }
}
