package com.example.parkezkotlin.data.network

import com.example.parkezkotlin.core.RetrofitHelper
import com.example.parkezkotlin.data.model.parkingModel
import kotlinx.coroutines.withContext
import retrofit2.Response
class parkingService {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getParkings(): List<parkingModel> {
        return withContext(kotlinx.coroutines.Dispatchers.IO) {

            val response: Response<List<parkingModel>> = retrofit.create(parkingApiClient::class.java).getParkings()
            response.body() ?: emptyList()
        }
    }
}