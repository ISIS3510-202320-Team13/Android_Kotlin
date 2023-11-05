package com.example.parkezkotlin.data.network

import com.example.parkezkotlin.core.RetrofitHelper
import com.example.parkezkotlin.data.model.parkingModel
import retrofit2.Response

class parkingService {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val apiClient = retrofit.create(parkingApiClient::class.java)

    suspend fun getParkings(): List<parkingModel> {
        val response: Response<List<parkingModel>> = apiClient.getParkings()

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error fetching parkings: ${response.message()}")
        }
    }
}
