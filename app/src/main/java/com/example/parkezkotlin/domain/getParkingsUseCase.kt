package com.example.parkezkotlin.domain

import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.data.parkingRepository

class getParkingsUseCase {
    private val repository = parkingRepository()
    suspend operator fun invoke():List<parkingModel>?= repository.getAllParkings()


}