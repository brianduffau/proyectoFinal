package com.example.proyectofinal.entities

import java.util.*

data class Review(
    val id_hiring: String = "",
    val stars: Int = 0,
    val content: String= "",
    val date: Date? = null
)
