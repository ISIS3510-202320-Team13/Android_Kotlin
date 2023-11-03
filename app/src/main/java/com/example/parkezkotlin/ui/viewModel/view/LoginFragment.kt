package com.example.parkezkotlin.ui.viewModel.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentLoginBinding

import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        val emailEditText = binding.username
        val passwordEditText = binding.passwordLogIn
        val loginButton = binding.login

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
                // Only attempt login if both email and password are not empty
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mapsFragment)
                    } else {
                        Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
