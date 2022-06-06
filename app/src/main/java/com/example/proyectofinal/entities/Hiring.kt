package com.example.proyectofinal.entities

import com.google.firebase.Timestamp

data class Hiring(
    val id_customer: String = "",
    val id_professional: String = "",
    val date: Timestamp? = null,
)