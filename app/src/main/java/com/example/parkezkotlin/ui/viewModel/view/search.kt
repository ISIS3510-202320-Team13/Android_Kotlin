package com.example.parkezkotlin.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.FragmentSearch2Binding
import com.example.parkezkotlin.ui.ParkingAdapter
import ParkingViewModel
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar

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

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = parkingAdapter

        viewModel.parkingsLiveData.observe(viewLifecycleOwner) { parkings ->
            if (parkings != null) {
                parkingAdapter.setAllParkings(parkings)
            } else {
                Snackbar.make(
                    binding.root,
                    "Error al cargar los estacionamientos",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.fetchParkings()

        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Cierra el teclado al enviar la consulta
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                parkingAdapter.filter(newText ?: "")
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.searchView2.windowToken, 0)
    }
}
