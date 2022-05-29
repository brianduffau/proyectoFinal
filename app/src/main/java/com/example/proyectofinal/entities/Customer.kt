package com.example.proyectofinal.entities

import android.net.Uri

data class Customer(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val img: String = "",
    val pets: Array<String>? = null
)