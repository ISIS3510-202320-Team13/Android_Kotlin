package com.example.parkezkotlin.data.network

import com.example.parkezkotlin.data.model.parkingModel
import retrofit2.http.GET
import retrofit2.Response

interface parkingApiClient {

    @GET("/parkings/all")
    suspend fun getParkings(): Response<List<parkingModel>>


}