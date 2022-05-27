package com.example.proyectofinal.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.R

class PetHolder(v: View): RecyclerView.ViewHolder(v) {

    private var view: View
    var cardPets : CardView
    init {
        this.view = v
        this.cardPets = view.findViewById(R.id.card_pets_list)
    }

    fun setPet(name: String, age: Int, type: String, image: String) {
        val agePet : TextView = view.findViewById(R.id.petAge)
        val namePet : TextView = view.findViewById(R.id.petName)
        val typePet : TextView = view.findViewById(R.id.petType)
        val imagePet : ImageView = view.findViewById(R.id.petImage)

        agePet.text = age.toString()
        namePet.text = name
        typePet.text = type
        Glide.with(this.view).load(image).into(getImageView())

    }


    fun getImageView () : ImageView {
        return view.findViewById(R.id.petImage)
    }
    fun getCard (): CardView {
        return view.findViewById(R.id.card_pets_list)
    }

}