package com.example.parkezkotlin.data.model

import com.google.firebase.firestore.GeoPoint

data class parkingModel (
    val coordinates: GeoPoint? = null,
    val direccion: String? = null,
    val availabilityCars: Int? = null,
    val availabilityMotorcycle: Int? = null,
    val price: Int? = null,
    val rating: Double? = null,
    val name: String? = null,
    val uid: String? = null
) {
    constructor() : this(null, null, null, null, null, null, null, null)
}
