package com.example.proyectofinal.entities

import android.net.Uri


data class Pet(
    val id: String = "",
    val imgPet: String = "",
    val name: String = "",
    val age: Int = 0,
    val type: String = "",
    val idOwner: String = "",
    val disp: Boolean = true,
)