package com.example.parkezkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.navigation.Navigation
import com.example.parkezkotlin.databinding.ActivityMainBinding
import com.example.parkezkotlin.databinding.FragmentMapsBinding
import com.example.parkezkotlin.databinding.FragmentSearch2Binding


/**
 * A simple [Fragment] subclass.
 * Use the [search.newInstance] factory method to
 * create an instance of this fragment.
 */
class search : Fragment() {


    private lateinit var binding : FragmentSearch2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearch2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parking = arrayOf("Parking 1", "Parking 2", "Parking 3", "Parking 4", "Parking 5", "Parking 6", "Parking 7", "Parking 8", "Parking 9", "Parking 10")
        val current_parking = binding.cardView

        current_parking.setOnClickListener {
            // go to the payment fragment
            Navigation.findNavController(view).navigate(R.id.action_searchFragment2_to_parkingDetail)
        }
    }
}