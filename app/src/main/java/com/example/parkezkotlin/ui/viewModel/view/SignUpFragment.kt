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

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        if (emailEditText.text.toString().isEmpty() || emailConfirmEditText.text.toString().isEmpty()) {
            emailEditText.error = "Please enter email"
            emailEditText.requestFocus()
        }
        if (passwordEditText.text.toString().isEmpty() || confirmPasswordEditText.text.toString().isEmpty()) {
            passwordEditText.error = "Please enter password"
            passwordEditText.requestFocus()
        }
        if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
            confirmPasswordEditText.error = "Passwords do not match"
            confirmPasswordEditText.requestFocus()
        }
        if (emailEditText.text.toString() != emailConfirmEditText.text.toString()) {
            emailConfirmEditText.error = "Emails do not match"
            emailConfirmEditText.requestFocus()
        }
        signUpButton.setOnClickListener {
            firebaseAuth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_signUpFragment2_to_main)
                } else {
                    Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}