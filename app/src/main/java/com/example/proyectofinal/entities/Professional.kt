package com.example.proyectofinal.entities

import com.google.firebase.firestore.GeoPoint

data class Professional(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val img: String = "",
    val phone: String = "",
    val geo: GeoPoint? = null,
    val polygon: String? = null,
    val professionalType: String = "",
    val petType: String = "",
    val petAge: Array<Int>? = null,
    val petQty: Int = 1,
)