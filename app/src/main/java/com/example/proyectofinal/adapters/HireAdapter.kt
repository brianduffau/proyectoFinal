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
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Hiring


class HireAdapter(var context: Context,
                  var hiresList: ArrayList<Hiring>,
                  var onClick: (Int) -> Unit): RecyclerView.Adapter<HireAdapter.HireHolder>() {

    class HireHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View
        var cardHires : CardView
        init {
            this.view = v
            this.cardHires = view.findViewById(R.id.card_hires_list)
        }

        fun setNameProf(name: String) {
            val nameProf : TextView = view.findViewById(R.id.profHireName)
            nameProf.text = name
        }

        fun setTypeProf(type: String) {
            val typeProf: TextView = view.findViewById(R.id.profHireType)
            typeProf.text = type
        }

        fun getImageView () : ImageView {
            return view.findViewById(R.id.profHireImage)
        }
        fun getCard (): CardView {
            return view.findViewById(R.id.card_hires_list)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hire_item, parent, false)
        return (HireHolder(itemView))
    }

    override fun onBindViewHolder(holder: HireHolder, position: Int) {


        holder.setNameProf(hiresList[position].professional_name)
        holder.setTypeProf(hiresList[position].professional_type)

        // PARA QUE CARGUE LA IMAGEN:
        //Glide.with(context).load(hiresList[position].profImg).into(holder.getImageView())*/

        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return hiresList.size
    }

}