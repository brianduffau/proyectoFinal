package com.example.proyectofinal.entities

data class Pet(
    var name: String = "",
    var age: Int = -1,
    var type: String = "",
    var idPet: String = "",
    var imgPet: String = "",
    var idOwner: String = "") {}
// OPCION 1 - CON HOLDER Y FUN DE ADAPTER OP 1
/*data class Pet(
    var name: String ?= null,
    var age: Int ?= null,
    var type: String ?= null,
    var idPet: String ?= null,
    var imgPet: String ?= null,
    var idOwner: String ?= null) {}*/

// OPCION 2 - CON HOLDER Y FUN DE ADAPTER OP 2
/*class Pet(
    var idPet: String,
    var imgPet: String,
    var name: String,
    var age: Int,
    var type: String,
    var idOwner: String
)
{

    constructor() : this("","","",0,"","")

}*/


/*
    var idPet: String = ""
    var imgPet: String = ""
    var name: String = ""
    var age: Int = 0
    var type: String = ""
    var idOwner: String = ""
*/



