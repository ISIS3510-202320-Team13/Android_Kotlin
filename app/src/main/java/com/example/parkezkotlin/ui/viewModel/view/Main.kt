package com.example.parkezkotlin.ui.viewModel.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.NavigationRail
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentMainBinding
import com.google.firebase.database.core.Context
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class main : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        //set up action for button

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = activity?.getSharedPreferences("FILE_KEY", android.content.Context.MODE_PRIVATE)!!
        if(sharedPreferences.contains("email")){
            Navigation.findNavController(view).navigate(R.id.action_main_to_mapsFragment)
        }
        super.onViewCreated(view, savedInstanceState)
        val button = binding.button2
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_main_to_signUpFragment2)
        }
        val button2 = binding.button3
        button2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_main_to_loginFragment)
            val database = Firebase.database
            val myRef = database.getReference("message")
            myRef.setValue("Hello, World!")


        }
    }

}
