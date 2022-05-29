package com.example.proyectofinal.entities

import com.google.firebase.firestore.GeoPoint

data class Professional(
    val id: String = "",
    val mail: String = "",
    val name: String = "",
    val surname: String = "",
    val img: String = "",
    val phone: String = "",
    val geo: GeoPoint? = null,
    val polygon: String? = null,
    val professionalType: Array<String>? = null,
    val petType: Array<String>? = null,
    val petAge: Array<Int>? = null,

    )