package com.example.proyectofinal.entities

class Customer (
    id: String,
    mail: String,
    password: String,
    name: String,
    surname: String,
    phone: String,
    pets: Array<Pet>

): User(id,mail, password,
    name,
    surname, phone
)