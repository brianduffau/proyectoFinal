package com.example.proyectofinal.entities

class Professional (
    id: String,
    mail: String,
    pass: String,
    name: String,
    surname: String,
    phone: String,
    address: String,
    lat: Float,
    long: Float,
    polygon: String,
    professionalType: Array<String>, //o Enum
    petType: Array<PetType>,
    petAge: Array<Integer>,


): User(id,mail, pass, name,
    surname,
    phone, address
)