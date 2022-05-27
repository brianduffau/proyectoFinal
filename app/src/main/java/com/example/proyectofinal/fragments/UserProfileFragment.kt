package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.UserProfileViewModel

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var viewModel: UserProfileViewModel
    private lateinit var petsButton: Button
    private lateinit var hiringsButton: Button

    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_profile, container, false)

        petsButton = v.findViewById(R.id.myPets)
        hiringsButton = v.findViewById(R.id.myHirings)

        petsButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionUserPtoPets)}
        hiringsButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionUserPtoHirings)}

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}