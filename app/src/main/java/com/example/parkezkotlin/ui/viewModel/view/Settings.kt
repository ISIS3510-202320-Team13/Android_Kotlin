package com.example.parkezkotlin.ui.viewModel.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentMainBinding
import com.example.parkezkotlin.databinding.FragmentSettingsBinding
import com.example.parkezkotlin.databinding.FragmentSignUpBinding
import kotlin.math.log

class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun onCreate(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("FILE_KEY", Context.MODE_PRIVATE)!!
        val editor = sharedPreferences.edit()
        val logoutBtn = binding.logout
        val email = binding.email

        logoutBtn.setOnClickListener{
            editor.clear()
            editor.apply()
            Navigation.findNavController(view).navigate(R.id.action_settings_to_mapsFragment)
        }
    }

}


