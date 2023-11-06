package com.example.parkezkotlin.ui.viewModel.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!
    private var isConnected = true
    // BroadcastReceiver para escuchar cambios en la conectividad
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentConnectivity = isNetworkAvailable()
            if (!isConnected && currentConnectivity) {
                Toast.makeText(context, "Conexión a Internet restaurada.", Toast.LENGTH_SHORT).show()
            }
            isConnected = currentConnectivity
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.imageView18
        backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_main)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        // Recuperar el email del cache si existe
        val cachedEmail = context?.getSharedPreferences("user_cache", Context.MODE_PRIVATE)?.getString("email", "")
        if (!cachedEmail.isNullOrEmpty()) {
            binding.username.setText(cachedEmail)
        }

        val emailEditText = binding.username
        val passwordEditText = binding.passwordLogIn
        val loginButton = binding.login

        sharedPreferences = activity?.getSharedPreferences("FILE_KEY", Context.MODE_PRIVATE)!!

        if(sharedPreferences.contains("email")){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mapsFragment)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) {
                emailEditText.error = "Please enter email"
                emailEditText.requestFocus()
            } else if (password.isEmpty()) {
                passwordEditText.error = "Please enter password"
                passwordEditText.requestFocus()
            } else {
                if (isNetworkAvailable()) {
                    // Intenta iniciar sesión solo si hay conexión y ambos campos no están vacíos
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val editor = sharedPreferences.edit()
                            editor.putString("email", email)
                            editor.putString("password", password)
                            editor.apply()
                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mapsFragment)
                        } else {
                            Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(activity, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
                    // Guardar el email en el cache para recuperarlo más tarde
                    context?.getSharedPreferences("user_cache", Context.MODE_PRIVATE)?.edit()?.apply {
                        putString("email", email)
                        apply()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Registrar el BroadcastReceiver para escuchar cambios en la conectividad
        context?.registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        // Desregistrar el BroadcastReceiver cuando el fragmento no esté visible
        context?.unregisterReceiver(connectivityReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Método para comprobar si hay conexión a Internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = connectivityManager?.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
