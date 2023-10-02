package com.example.parkezkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.parkezkotlin.databinding.FragmentMainBinding


class main : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        //set up action for button

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = binding.button2
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_main_to_signUpFragment2)
        }
        val button2 = binding.button3
        button2.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_main_to_loginFragment)
        }
    }

}
