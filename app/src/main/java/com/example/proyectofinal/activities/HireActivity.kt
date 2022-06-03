package com.example.proyectofinal.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Professional
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HireActivity : AppCompatActivity() {

    var db = Firebase.firestore

    lateinit var professional: Professional


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val professionalId = intent.getStringExtra("professionalId")
        if (professionalId != null) {
            getProfessional(professionalId)
        }
    }


    private fun getProfessional(professionalId: String) {
        val collection = db.collection("professionals").document(professionalId)

        collection.get().addOnSuccessListener { document ->
            if(document != null){
                professional = document.toObject<Professional>()!!
                setContentView(R.layout.activity_hire)
            } else {
                Log.d("documentNotFound", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("documentNotOK", "get failed with ", exception)
            }
    }



}