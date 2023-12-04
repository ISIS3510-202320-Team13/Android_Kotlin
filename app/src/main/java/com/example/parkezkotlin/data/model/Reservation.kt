package com.example.parkezkotlin.data.model

data class Reservation(
val cost: String?,
val entry_time: String?,
val exit_time: String?,
val parking: String?,
val status: String?,
val time_to_reserve: Int?,
val user: String?,
){
    constructor() : this(null, null, null, null, null, null, null)
}



