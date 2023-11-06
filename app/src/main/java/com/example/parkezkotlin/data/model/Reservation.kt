package com.example.parkezkotlin.data.model

data class Reservation(
    val cost: Int,
    val entryTime: Long,
    val exitTime: Long,
    val parkingId: String,
    val status: String,
    val userId: String
)
