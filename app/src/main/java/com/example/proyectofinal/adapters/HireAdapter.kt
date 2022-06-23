package com.example.proyectofinal.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
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
import java.util.*


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

            val calMsg : TextView = view.findViewById(R.id.calMsg)
            calMsg.setVisibility(View.GONE);
        }

        fun PuntajeLinkProf(id: String, position: Int, hiringId: String) {
            val reviewLink : Button = view.findViewById(R.id.reviewLink)

            val ratingContrataciones : RatingBar = view.findViewById(R.id.ratingContrataciones)
            ratingContrataciones.setVisibility(View.GONE);

            val action = MyHiringsFragmentDirections.actionHiringsFragmentToReviewFragment2(id, hiringId)
            reviewLink.setOnClickListener{ Navigation.findNavController(view).navigate(action)}

            val calMsg : TextView = view.findViewById(R.id.calMsg)
            calMsg.setVisibility(View.GONE);
        }

        fun showOnlyProntoPodraCalificar() {
            val reviewLink : Button = view.findViewById(R.id.reviewLink)
            reviewLink.setVisibility(View.GONE);

            val ratingContrataciones : RatingBar = view.findViewById(R.id.ratingContrataciones)
            ratingContrataciones.setVisibility(View.GONE);
        }

        fun setNameProf(name: String) {
            val nameProf : TextView = view.findViewById(R.id.profHireName)
            nameProf.text = name
        }

        fun setFechaProf(fechaParseada: String) {
            val fecha : TextView = view.findViewById(R.id.fecha)
            fecha.text = fechaParseada
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

    fun checkCalificacion(id: String, holder: HireHolder, position: Int, inTime: Boolean, hiringId: String) {
        var count = 0;
        var review: Review

        db.collection("reviews")
            .whereEqualTo("id_reviewer", userId())
            .whereEqualTo("id_reviewed", id)
            .whereEqualTo("reviewId", hiringId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (h in snapshot) {
                        count++
                        review = h.toObject()
                        holder.HideButtonAndShowStars(review.stars)
                    }
                    if(count == 0){
                        if(inTime) {
                            holder.PuntajeLinkProf(hiresList[position].id_professional, position, hiringId)
                        }else{
                            holder.showOnlyProntoPodraCalificar()
                        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: HireHolder, position: Int) {
        val timestamp = System.currentTimeMillis() / 1000
        var inTime = false;

        if(timestamp > hiresList[position].endDate!!.seconds){
            inTime = true
        }

        val startDate = hiresList[position].startDate?.toDate()
        val endDate = hiresList[position].endDate?.toDate()

        val startDateFormated = "Desde " + DateFormat.format("dd-MM-yyyy hh:mm",startDate).toString()
        val endDateFormated = "Hasta " + DateFormat.format("dd-MM-yyyy hh:mm",endDate).toString()

        val finalDate = "$startDateFormated-$endDateFormated"

        holder.setNameProf(hiresList[position].professional_name)
        holder.setTypeProf(hiresList[position].professional_type)
        holder.setFechaProf(finalDate)

        var hiringId = hiresList[position].hiringId

        if (hiringId != null) {
            checkCalificacion(hiresList[position].id_professional, holder, position, inTime, hiringId)
        }

        if(!hiresList[position].professional_img.isEmpty()) {
            Picasso.get().load(hiresList[position].professional_img).fit().centerCrop()
                .into(holder.getImageView())
        }

    }

    override fun getItemCount() = hiresList.size

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

}