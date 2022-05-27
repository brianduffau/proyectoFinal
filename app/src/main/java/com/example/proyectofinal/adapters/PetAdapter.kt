package com.example.proyectofinal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Pet
import com.example.proyectofinal.holders.PetHolder

class PetAdapter(var context: Context,
                 var petsList: ArrayList<Pet>,
                 var onClick: (Int) -> Unit): RecyclerView.Adapter<PetHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return (PetHolder(itemView))
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.setPet(petsList[position].name, petsList[position].age, petsList[position].type,petsList[position].imgPet)

        holder.getCard().setOnClickListener{
            onClick(position) // para implementarlo en el fragment
        }
        /*holder.cardPets.setOnClickListener {
            System.out.println(petsList[position].name)
            System.out.println(petsList[position].toString())

            var params = Bundle()

            params.putString("nombre",petsList[position].name)
            params.putString("foto",petsList[position].imgPet)
            params.putString("edad",petsList[position].age.toString())
            params.putString("tipo",petsList[position].type)

            view.findNavController().navigate(R.id.recipeDetailsFragment, params)
        }*/
    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}
