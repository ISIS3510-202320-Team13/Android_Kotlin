package com.example.parkezkotlin.ui.viewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkezkotlin.data.model.Reservation
import com.example.parkezkotlin.databinding.ItemReservationBinding

class ReservationAdapter : ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder>(
    ReservationDiffCallback()
) {

    private var allReservations: List<Reservation> = emptyList()

    fun setAllReservations(reservations: List<Reservation>) {
        allReservations = reservations
        submitList(reservations)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            allReservations
        } else {
            allReservations.filter { reservation ->
                reservation.parking?.contains(query, ignoreCase = true) == true
            }
        }
        submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = getItem(position)
        holder.bind(reservation)
    }

    inner class ReservationViewHolder(private val binding: ItemReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                // Obtén el parqueadero seleccionado
                val selectedreservation = getItem(adapterPosition)

                val args = Bundle()
                args.putString("reservation_id", selectedreservation.uid)
                println("reservation selected: ${selectedreservation.uid}")
                val navController=it.findNavController()

            }
        }

        fun bind(reservation: Reservation) {
            binding.textView43.text = reservation.parking
            val day=reservation.entry_time
            if (day != null) {
                if (day.length>=10)
                    binding.textView13.text = "Fecha: ${day.substring(0,10)}"
                else {
                    binding.textView13.text = "Horas: ${day}"
                }
            }
            binding.textView47.text = "Duración: ${reservation.time_to_reserve} minutos"
        }
    }

    class ReservationDiffCallback : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }
    }
}
