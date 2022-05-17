package com.example.proyectofinal.entities

import com.google.firebase.firestore.GeoPoint

class Professional (
    id: String,
    mail: String,
    pass: String,
    name: String,
    surname: String,
    phone: String,
    geo: GeoPoint,
    polygon: String,
    professionalType: Array<String>,
    petType: Array<String>,
    petAge: Array<Integer>,


): User(id,mail, pass, name,
    surname,
    phone
)