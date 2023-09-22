package com.example.parkezkotlin.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.parkezkotlin.databinding.MainpageBinding

class MainFragment : Fragment() {

    private var _binding: MainpageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainpageViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = MainpageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView
        mainpageViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}