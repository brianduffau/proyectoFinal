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
import com.example.proyectofinal.entities.Review


class ReviewAdapter( var context: Context,
                  var reviewsList: ArrayList<Review>,
                  var onClick: (Int) -> Unit): RecyclerView.Adapter<ReviewAdapter.ReviewHolder>(){

    class ReviewHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View
        var cardPets : CardView
        init {
            this.view = v
            this.cardPets = view.findViewById(R.id.card_reviews_list)
        }

        fun setNameCustomer(name: String) {
            val nameCust : TextView = view.findViewById(R.id.customerNameReview)
            nameCust.text = name
        }

        fun setComent(coment: String) {
            val comentCust : TextView = view.findViewById(R.id.comentReview)
            comentCust.text = coment
        }

        fun getImageView () : ImageView {
            return view.findViewById(R.id.customerImgReview)
        }
        fun getCard (): CardView {
            return view.findViewById(R.id.card_reviews_list)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return (ReviewHolder(itemView))
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {

        //holder.setNameCustomer(reviewsList[position])
        holder.setComent(reviewsList[position].content)

        // PARA QUE CARGUE LA IMAGEN:
        //Glide.with(context).load(reviewsList[position]).into(holder.getImageView())

        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }
}