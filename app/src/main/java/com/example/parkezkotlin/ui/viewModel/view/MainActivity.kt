package com.example.parkezkotlin.ui.viewModel.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.parkezkotlin.databinding.ActivityMainBinding
import com.example.parkezkotlin.databinding.FragmentParkingDetailBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var parkingBinding: FragmentParkingDetailBinding


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    }
}