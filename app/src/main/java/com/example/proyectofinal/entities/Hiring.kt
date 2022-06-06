package com.example.proyectofinal.entities

import com.google.firebase.Timestamp
import java.util.*


data class Hiring(
    val id_customer: String = "",
    val id_professional: String = "",
    val startDate: Timestamp? = null,
    val endDate: Timestamp? = null
)

