package com.example.proyectofinal.fragments.MainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.proyectofinal.R
import com.example.proyectofinal.adapters.HireAdapter
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.viewmodels.ReviewViewModel
import com.example.proyectofinal.viewmodels.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*


class ReviewFragment : Fragment() {

    private lateinit var viewModel: ReviewViewModel

    lateinit var v: View
    var db = Firebase.firestore

    private lateinit var toolbarText : TextView
    private lateinit var backButton : ImageView

    lateinit var addReview : Button
    lateinit var reviewText : TextView
    lateinit var puntajeBar: RatingBar

    var customer : Customer = Customer()

    val args: ReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_review, container, false)

        addReview = v.findViewById(R.id.addReview)
        reviewText = v.findViewById(R.id.reviewText)
        puntajeBar = v.findViewById(R.id.puntajeBar)

        val profesionalId = args.profesionalId


        getUserInfo()

        addReview.setOnClickListener{addReview(
            profesionalId,
            reviewText.getText().toString(),
            puntajeBar.getRating()
        )
            Snackbar.make(v,"Review agregada con exito", Snackbar.LENGTH_SHORT).show()}

        return v
    }

    private fun addReview(profesionalId: String, reviewText: String, puntajeBar: Float) {

        val data = mapOf(
            "reviewer_name" to  customer.name,
            "reviewer_surname" to  customer.surname,
            "reviewer_photo" to  customer.img,
            "id_reviewer" to  customer.id,
            "id_reviewed" to  profesionalId,
            "stars" to  puntajeBar,
            "content" to  reviewText,
        )

        db.collection("reviews")
            .add(data)
            .addOnSuccessListener { document ->
                Log.d("ReviewOk", "Review con ID: ${document.id}")

            }
            .addOnFailureListener { exception ->
                Log.w("falloAddReview", "Error getting documents: ", exception)
            }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

    @SuppressLint("SetTextI18n")
    private fun setupToolbar() {
        toolbarText.setText("Mis Contrataciones")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }

    fun getUserInfo() {
        userId()
        db.collection("customers")
            .whereEqualTo("id", "6xFklVWFRgY52uAwTI2zuctAE1O2")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (h in snapshot) {
                        customer = h.toObject()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

    }


    private fun userId (): String {
        val user = Firebase.auth.currentUser
        var email : String = ""
        if (user != null) {
            email = user.email.toString()
            Log.d("EMAIL", email)

        }
        return email
    }

}