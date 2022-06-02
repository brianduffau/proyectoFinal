package com.example.proyectofinal.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Customer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(){

    private val db = Firebase.firestore
    lateinit var userLog: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        val user = Firebase.auth.currentUser
        if (user != null) {
            getUserInfo(user.uid)
        }

    }


    fun getUserInfo(userId : String) {
        val docRef = db.collection("customers").document(userId)
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

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("listenerUserNotOk", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("listenerUserOk", "Current data: ${snapshot.data}")
            } else {
                Log.d("listenerUserNull", "Current data: null")
            }
        }
    }


}




