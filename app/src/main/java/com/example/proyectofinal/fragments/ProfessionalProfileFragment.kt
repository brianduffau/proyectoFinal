package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.adapters.HireAdapter
import com.example.proyectofinal.adapters.ReviewAdapter
import com.example.proyectofinal.entities.Hiring
import com.example.proyectofinal.entities.Review
import com.example.proyectofinal.viewmodels.ProfessionalProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfessionalProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfessionalProfileFragment()
    }

    private lateinit var viewModel: ProfessionalProfileViewModel

    lateinit var v: View
    lateinit var recReviews : RecyclerView
    lateinit var adapter: ReviewAdapter
    var db = Firebase.firestore
    var reviewsList : ArrayList<Review> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_professional_profile, container, false)

        recReviews = v.findViewById(R.id.recReviews)
        // ESTO ACA O EN EL ONSTART
        recReviews.setHasFixedSize(true)
        recReviews.layoutManager = LinearLayoutManager(context)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        reviewsBD()

    }

    fun reviewsBD () {
        db.collection("reviews")
            //.whereEqualTo("id_reviewed", profId())
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    Log.i("ifReview", "entro")
                    for (h in snapshot) {
                        reviewsList.add(h.toObject())
                        // EL TEMA ES QUE ACA DEBERIA AGREGAR DISTINTO A LO QUE ESTA EN LA CLASE, SON LOS NOMBRES, NO LOS ID. LA BUSQUEDA LA HAGO ACA MISMO?
                    }
                    // YO TENDRIA QUE BUSCAR ESE ID DE CADA UNO EN LA COLECCION DE PROFESIONALES PARA VER EL NOMBRE
                    adapter = ReviewAdapter(requireContext(),reviewsList){position->
                        Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
                        Log.i("entro for y adapter - r", "REVIEWS: $reviewsList")
                    }
                    recReviews.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

    }

}