package com.example.parkezkotlin.data.model

data class Reservation(
    val cost: String,
    val entryTime: String,
    val exitTime: Long,
    val parkingId: String,
    val status: String,
    val userId: String
)
