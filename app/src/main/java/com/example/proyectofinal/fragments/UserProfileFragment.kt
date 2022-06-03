package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.MainActivity
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.SharedViewModel
import com.example.proyectofinal.viewmodels.UserProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var viewModel: UserProfileViewModel


    private lateinit var petsButton: Button
    private lateinit var hiringsButton: Button
    private lateinit var modifyButton: Button

    lateinit var textName : TextView
    lateinit var textSurname : TextView
    lateinit var textMail : TextView
    lateinit var image : ImageView

    lateinit var user : Customer


    lateinit var v : View
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_profile, container, false)

        textName = v.findViewById(R.id.nameUser)
        textSurname = v.findViewById(R.id.surnameUser)
        textMail = v.findViewById(R.id.mailUser)
        image = v.findViewById(R.id.imageUser)

        petsButton = v.findViewById(R.id.myPets)
        hiringsButton = v.findViewById(R.id.myHirings)
        modifyButton = v.findViewById(R.id.modifyProfile)

        petsButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionUserPtoPets)}
        hiringsButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionUserPtoHirings)}
        modifyButton.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionModifyProfileUser)}

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        user = viewModel.getUserInfo()
        textName.text = user.name
        textMail.text = user.email
        textSurname.text = user.surname
        Glide.with(this).load(user.img).into(image)




    }




    /*
        textName.text = document.data?.get("name") as String
        textSurname.text = document.data?.get("surname") as String
        textMail.text = document.data?.get("email") as String
     */

}