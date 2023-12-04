package com.example.parkezkotlin.data.model

import com.google.firebase.firestore.Exclude

data class Reservation(
val cost: String?,
val entry_time: String?,
val exit_time: String?,
val parking: String?,
val status: String?,
val time_to_reserve: Int?,
val user: String?,
@Exclude
var uid: String? = null
){
    constructor() : this(null, null, null, null, null, null, null)
}



