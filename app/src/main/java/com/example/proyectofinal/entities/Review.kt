package com.example.proyectofinal.entities

data class Review(
    val reviewer_name: String = "",
    val reviewer_surname: String = "",
    val reviewer_photo: String = "",
    val id_reviewer: String = "",
    val id_reviewed: String = "",
    val stars: Float = 0.0F,
    val content: String= "",
    val reviewId: String? = "",
)
