package com.example.parkezkotlin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.databinding.ItemParkingBinding

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
