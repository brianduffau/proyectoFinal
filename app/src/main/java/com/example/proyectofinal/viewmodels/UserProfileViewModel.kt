package com.example.proyectofinal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.entities.Customer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserProfileViewModel : ViewModel() {

    private val db = Firebase.firestore
    lateinit var userLog: Customer

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

    fun getUserInfo() : Customer {

        val id = userId()
        val docRef = db.collection("customers").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("userOK", "DocumentSnapshot data: ${document.id}")
                    userLog = document.toObject<Customer>()!!
                } else {
                    Log.d("userNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("userNotOK", "get failed with ", exception)
            }
        return userLog

    }
}