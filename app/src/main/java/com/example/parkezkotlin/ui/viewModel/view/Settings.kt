package com.example.parkezkotlin.ui.viewModel.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentMainBinding
import com.example.parkezkotlin.databinding.FragmentSettingsBinding
import com.example.parkezkotlin.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log
class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("FILE_KEY", Context.MODE_PRIVATE)

        loadUserData()

        binding.Reservas.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_settings_to_pastReservations)
        }

        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            sharedPreferences.edit().clear().apply()
            Navigation.findNavController(view).navigate(R.id.action_settings_to_main)
        }
    }

    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        binding.email.text = document.getString("email") ?: "No Email"
                        binding.name.text = document.getString("name") ?: "No Name"

                        val imageUrl = document.getString("picture")
                        if (!imageUrl.isNullOrEmpty()) {
                            // Cargar la imagen con Glide
                            Glide.with(this)
                                .load(imageUrl)
                                .into(binding.imageView20)
                        }
                    } else {
                        Toast.makeText(context, "No se encontró la información del usuario.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener los detalles", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}