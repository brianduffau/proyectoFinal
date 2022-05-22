package com.example.proyectofinal.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button

import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.proyectofinal.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        bottomNavView = findViewById(R.id.bottom_bar)
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

    }



}











/*//prueba Firebase
        val db = Firebase.firestore
        val user = hashMapOf(
            "nombre" to "22222Ada",
            "apellido" to "Lovelace",
            "mail" to "ada@gmail.com",
            "direccion" to "Rivadavia 133",
            "latlong" to LatLng(-34.0, 151.0)
        )
        db.collection("usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Usuario agregado con id : ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }*/