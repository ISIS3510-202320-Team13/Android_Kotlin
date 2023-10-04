package com.example.parkezkotlin.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.parkezkotlin.databinding.FragmentLoginBinding

import com.example.parkezkotlin.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val backButton = binding.imageButton2
        backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_main)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val emailEditText = binding.username
        val passwordEditText = binding.passwordLogIn
        val loginButton = binding.login
        if(emailEditText.text.toString().isEmpty()){
            emailEditText.error = "Please enter email"
            emailEditText.requestFocus()
        }
        if(passwordEditText.text.toString().isEmpty()){
            passwordEditText.error = "Please enter password"
            passwordEditText.requestFocus()
        }

        loginButton.setOnClickListener {
            firebaseAuth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mapsFragment)
                }else{
                    Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}