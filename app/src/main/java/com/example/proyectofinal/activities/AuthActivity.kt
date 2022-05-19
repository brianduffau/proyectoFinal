package com.example.proyectofinal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.fragments.AuthActivity.AuthFragment


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        showFragment(AuthFragment())

    }

    private fun showFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.auth_container, fragment)
        transaction.commit()
    }





}