package com.example.parkezkotlin.data

import com.example.parkezkotlin.data.model.parkingModel
import com.example.parkezkotlin.data.network.parkingService
import com.example.parkezkotlin.data.model.ParkingProvider
class parkingRepository {
    private val api= parkingService()
    suspend fun getAllParkings() : List<parkingModel>{
        val response = api.getParkings()
        ParkingProvider.parkings= response
        return response
    }

}