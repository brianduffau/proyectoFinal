package com.example.proyectofinal.entities

import java.util.*

data class Review(
    val id_reviewer: String = "",
    val id_reviewed: String = "",
    val stars: Int = 0,
    val content: String= "",
)
