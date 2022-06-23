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
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.ReviewViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


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
        val hiringId = args.hiringId

        getUserInfo()

        addReview.setOnClickListener{addReview(
            profesionalId,
            reviewText.getText().toString(),
            puntajeBar.getRating(),
            hiringId
            )
        }

        return v
    }

    private fun addReview(profesionalId: String, reviewText: String, puntajeBar: Float, hiringId: String) {

        val data = mapOf(
            "reviewer_name" to  customer.name,
            "reviewer_surname" to  customer.surname,
            "reviewer_photo" to  customer.img,
            "id_reviewer" to  customer.id,
            "id_reviewed" to  profesionalId,
            "stars" to  puntajeBar,
            "content" to  reviewText,
            "reviewId" to hiringId,
        )

        db.collection("reviews")
            .add(data)
            .addOnSuccessListener { document ->
                Log.d("ReviewOk", "Review con ID: ${document.id}")
                Snackbar.make(v,"Review agregada con exito", Snackbar.LENGTH_SHORT).show()
                Navigation.findNavController(v).popBackStack()
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
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }

    fun getUserInfo() {
        db.collection("customers")
            .whereEqualTo("id", userId())
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
        var id : String = ""
        if (user != null) {
            id = user.uid
            Log.d("ID: ", id)

        }
        return id
    }

}