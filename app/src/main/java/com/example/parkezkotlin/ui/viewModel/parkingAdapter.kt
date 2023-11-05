package com.example.parkezkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.ItemParkingBinding
import com.example.parkezkotlin.R

class ParkingAdapter : ListAdapter<parkingModel, ParkingAdapter.ParkingViewHolder>(ParkingDiffCallback()) {

    private var allParkings: List<parkingModel> = emptyList()

    fun setAllParkings(parkings: List<parkingModel>) {
        allParkings = parkings
        submitList(parkings)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            allParkings
        } else {
            allParkings.filter { parking ->
                parking.name?.contains(query, ignoreCase = true) == true
            }
        }
        submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        val binding = ItemParkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        val parking = getItem(position)
        holder.bind(parking)
    }

    inner class ParkingViewHolder(private val binding: ItemParkingBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                // Obtén el parqueadero seleccionado
                val selectedParking = getItem(adapterPosition)

                val args = Bundle()
                args.putString("parking_id", selectedParking.uid)
                println("Parking selected: ${selectedParking.uid}")
                val navController=it.findNavController()

                // Navega al fragmento de detalle del parqueadero y pasa el ID como argumento para recuperarlo en el otro fragmento
                navController.navigate(R.id.action_searchFragment2_to_parkingDetail, args)
            }
        }

        fun bind(parking: parkingModel) {
            binding.textView43.text = parking.name
            binding.textView47.text = "${parking.availabilityCars} cupos disponibles - ${parking.price}/min"
        }
    }

    class ParkingDiffCallback : DiffUtil.ItemCallback<parkingModel>() {
        override fun areItemsTheSame(oldItem: parkingModel, newItem: parkingModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: parkingModel, newItem: parkingModel): Boolean {
            return oldItem == newItem
        }
    }
}
