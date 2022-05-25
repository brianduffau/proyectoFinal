package com.example.proyectofinal.fragments.AuthActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var v: View
    private lateinit var viewModel: HomeViewModel
    private lateinit var registerButton : Button
    private lateinit var loginButton : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        findViews()
        loginButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionHomeToAuth)}
        registerButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionHometoRegister)}


        return v
    }

    private fun findViews() {
        loginButton = v.findViewById(R.id.homeLoginButton)
        registerButton = v.findViewById(R.id.homeSignUpButton)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}