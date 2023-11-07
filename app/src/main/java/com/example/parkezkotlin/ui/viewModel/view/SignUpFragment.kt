package com.example.parkezkotlin.ui.viewModel.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class signUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    // BroadcastReceiver para escuchar cambios en la conectividad
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = binding.imageView19
        backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment2_to_main)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        val emailEditText = binding.username
        val emailConfirmEditText = binding.username2
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.password2
        val signUpButton = binding.signup
        val cachedEmail = context?.let { retrieveFromCache("cachedEmail", it) }
        if (!cachedEmail.isNullOrEmpty()) {
            emailEditText.setText(cachedEmail)
        }

        sharedPreferences = activity?.getSharedPreferences("FILE_KEY", Context.MODE_PRIVATE)!!

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val emailConfirm = emailConfirmEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isEmpty() || emailConfirm.isEmpty()) {
                emailEditText.error = "Please enter email"
                emailEditText.requestFocus()
            } else if (password.isEmpty() || confirmPassword.isEmpty()) {
                passwordEditText.error = "Please enter password"
                passwordEditText.requestFocus()
            } else if (password != confirmPassword) {
                confirmPasswordEditText.error = "Passwords do not match"
                confirmPasswordEditText.requestFocus()
            } else if (email != emailConfirm) {
                emailConfirmEditText.error = "Emails do not match"
                emailConfirmEditText.requestFocus()
            } else {
                if(!isNetworkAvailable()) {
                    Toast.makeText(context, "Sin conexión a Internet. Intente más tarde", Toast.LENGTH_SHORT).show()
                    context?.let { it1 -> saveToCache("cachedEmail", email, it1) }
                } else {
                    // Perform the sign-up process
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val editor = sharedPreferences.edit()
                                editor.putString("email", email)
                                editor.putString("password", password)
                                Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                                Navigation.findNavController(view).navigate(R.id.action_signUpFragment2_to_main)
                            } else {
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
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
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    private fun saveToCache(key: String, value: String, context: Context) {
        val sharedPref = context.getSharedPreferences("userCache", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
    private fun retrieveFromCache(key: String, context: Context): String? {
        val sharedPref = context.getSharedPreferences("userCache", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }


}
