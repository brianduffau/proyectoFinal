package com.example.proyectofinal.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Hiring
import com.example.proyectofinal.entities.Review
import com.example.proyectofinal.fragments.recyclerViews.MyHiringsFragmentDirections
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class HireAdapter(var context: Context,
                  var hiresList: ArrayList<Hiring>,
                  var onClick: (Int) -> Unit): RecyclerView.Adapter<HireAdapter.HireHolder>() {

    var db = Firebase.firestore

    class HireHolder(v: View): RecyclerView.ViewHolder(v) {

        private var view: View
        var cardHires : CardView
        init {
            this.view = v
            this.cardHires = view.findViewById(R.id.card_hires_list)
        }

        fun HideButtonAndShowStars(stars: Float) {
            val reviewLink : Button = view.findViewById(R.id.reviewLink)
            reviewLink.setVisibility(View.GONE);

            val ratingContrataciones : RatingBar = view.findViewById(R.id.ratingContrataciones)
            ratingContrataciones.setIsIndicator(true)
            ratingContrataciones.setRating(stars)
        }

        fun PuntajeLinkProf(id: String) {
            val reviewLink : Button = view.findViewById(R.id.reviewLink)

            val ratingContrataciones : RatingBar = view.findViewById(R.id.ratingContrataciones)
            ratingContrataciones.setVisibility(View.GONE);

            val action = MyHiringsFragmentDirections.actionHiringsFragmentToReviewFragment2(id)
            reviewLink.setOnClickListener{ Navigation.findNavController(view).navigate(action)}
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

    fun checkCalificacion(id: String, holder: HireHolder, position: Int) {
        var count = 0;
        var review: Review

        db.collection("reviews")
            .whereEqualTo("id_reviewer", userId())
            .whereEqualTo("id_reviewed", id)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (h in snapshot) {
                        count++
                        review = h.toObject()
                        holder.HideButtonAndShowStars(review.stars)
                    }
                    if(count == 0){
                        holder.PuntajeLinkProf(hiresList[position].id_professional)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hire_item, parent, false)
        return (HireHolder(itemView))
    }

    override fun onBindViewHolder(holder: HireHolder, position: Int) {

        holder.setNameProf(hiresList[position].professional_name)
        holder.setTypeProf(hiresList[position].professional_type)

        checkCalificacion(hiresList[position].id_professional, holder, position)

        if(!hiresList[position].professional_img.isEmpty()) {
            Picasso.get().load(hiresList[position].professional_img).fit().centerCrop()
                .into(holder.getImageView())
        }

    }

    override fun getItemCount(): Int {
        return hiresList.size
    }

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

}