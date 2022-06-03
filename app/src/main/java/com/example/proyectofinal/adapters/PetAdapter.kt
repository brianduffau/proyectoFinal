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
//import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Pet
import com.example.proyectofinal.holders.PetHolder

class PetAdapter( var context: Context,
                 var petsList: ArrayList<Pet>,
                 var onClick: (Int) -> Unit): RecyclerView.Adapter<PetHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return (PetHolder(itemView))
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {

        // OPCION 1 - CON HOLDER Y ENTITIE OP 1
        //val p : Pet = petsList[position]
        holder.namePet.text = petsList[position].name
        //holder.agePet.text = p.age.toString()
        //holder.typePet.text = p.type

        // OPCION 2 - CON HOLDER Y ENTITIE OP 2
        //holder.setPet(petsList[position].name, petsList[position].age, petsList[position].type)

        // PARA QUE CARGUE LA IMAGEN:
        //Glide.with(context).load(p.imgPet).into(holder.getImageView())

        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}
