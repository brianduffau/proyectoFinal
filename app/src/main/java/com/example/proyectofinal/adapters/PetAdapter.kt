package com.example.proyectofinal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Pet

class PetAdapter( var context: Context,
                 var petsList: ArrayList<Pet>,
                 var onClick: (Int) -> Unit): RecyclerView.Adapter<PetAdapter.PetHolder>(){

    class PetHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View
        var cardPets : CardView
        init {
            this.view = v
            this.cardPets = view.findViewById(R.id.card_pets_list)
        }

        fun setAge(age: Int) {
            val agePet : TextView = view.findViewById(R.id.petAge)
            agePet.text = age.toString()
        }

        fun setName(name: String) {
            val namePet : TextView = view.findViewById(R.id.petName)
            namePet.text = name
        }

        fun setType(type: String) {
            val typePet : TextView = view.findViewById(R.id.petType)
            typePet.text = type
        }

        fun getImageView () : ImageView {
            return view.findViewById(R.id.petImage)
        }
        fun getCard (): CardView {
            return view.findViewById(R.id.card_pets_list)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)

        return (PetHolder(itemView))
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {

        holder.setAge(petsList[position].age)
        holder.setName(petsList[position].name)
        holder.setType(petsList[position].type)

        // PARA QUE CARGUE LA IMAGEN:
        Glide.with(context).load(petsList[position].imgPet).into(holder.getImageView())

        holder.getCard().setOnClickListener{
            onClick(position)
        }


    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}
