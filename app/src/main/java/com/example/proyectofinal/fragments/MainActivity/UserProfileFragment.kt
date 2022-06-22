package com.example.proyectofinal.fragments.MainActivity

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
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
//import com.bumptech.glide.Glide
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.UserProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class UserProfileFragment : Fragment() {

    private lateinit var viewModel: UserProfileViewModel


    private lateinit var petsButton: Button
    private lateinit var hiringsButton: Button
    private lateinit var modifyButton: Button

    lateinit var textName : TextView
    lateinit var textSurname : TextView
    lateinit var image : ImageView
    //lateinit var textMail : TextView
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
        image = v.findViewById(R.id.imageUser)
        //textMail = v.findViewById(R.id.mailUser)

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
        viewModel.getUserInfo(textName,textSurname,image)
        //getUserInfo()
    }






    // PARA HACERLO SIN VIEW MODEL

    private fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }


    private fun getUserInfo() {
        val docRef = db.collection("customers").document(userId())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("userOK", "User id: ${userId()} y time: ${Calendar.getInstance().time}")
                    //user = document.toObject<Customer>()!!
                    //showData(user)
                    textName.text = document.data?.get("name") as String
                    textSurname.text = document.data?.get("surname") as String
                    //textMail.text = document.data?.get("email") as String
                    Log.d("userImgOK", "Imagen URL: ${document.data?.get("img") as String}")
                    if(document.data?.get("img") != ""){
                        Picasso.get().load(document.data?.get("img") as String).fit().centerCrop().into(image)
                    }

                } else {
                    Log.d("userNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("userNotOK", "get failed with ", exception)
            }
    }

    private fun showData(user: Customer) {
        textName.text = user.name
        //textMail.text = user.email
        textSurname.text = user.surname
        Picasso.get().load(user.img).fit().centerCrop().into(image)
        Log.d("userImgOK", "Url img: ${user.img}")
    }
}