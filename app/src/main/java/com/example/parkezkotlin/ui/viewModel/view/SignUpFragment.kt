package com.example.parkezkotlin.ui.viewModel.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.parkezkotlin.R
import com.example.parkezkotlin.databinding.FragmentSignUpBinding

import com.google.firebase.auth.FirebaseAuth

class signUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

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
                // Perform the sign-up process
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_signUpFragment2_to_main)
                        } else {
                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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
