package com.example.proyectofinal.fragments.MainActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.UpdateUserProfileViewModel
import com.example.proyectofinal.viewmodels.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateUserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateUserProfileFragment()
    }

    private lateinit var viewModel: UpdateUserProfileViewModel
    private lateinit var viewModelShared: UserProfileViewModel

    lateinit private var textName : EditText
    lateinit private var textSurname : EditText
    lateinit private var textMail : EditText
    lateinit private var buttonUpdate : Button
    //lateinit var user : Customer
    private val db = Firebase.firestore

    private lateinit var toolbarText : TextView
    private lateinit var backButton : ImageView

    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_update_user_profile, container, false)

        backButton = v.findViewById(R.id.back_button_toolbar)
        toolbarText = v.findViewById(R.id.text_toolbar)
        setupToolbar()

        textName = v.findViewById(R.id.nameUserUp)
        textSurname = v.findViewById(R.id.surnameUserUp)


        buttonUpdate = v.findViewById(R.id.updateProf)
        buttonUpdate.setOnClickListener{ updateUser()}

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateUserProfileViewModel::class.java)
        viewModelShared = ViewModelProvider(requireActivity()).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setupToolbar() {
        toolbarText.setText("Mi perfil")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }

    override fun onStart() {
        super.onStart()

        getUserInfo()

        //user = viewModelShared.getUserInfo()!!

        //textName.setText(user.name)
        //textSurname.setText(user.surname)

    }

    fun getUserInfo() {
        val docRef = db.collection("customers").document(viewModelShared.userId())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("userOK", "DocumentSnapshot data: ${document.id}")
                    textName.setText(document.data?.get("name") as String)
                    textSurname.setText(document.data?.get("surname") as String)
                } else {
                    Log.d("userNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("userNotOK", "get failed with ", exception)
            }
    }

    private fun updateUser() {

        var name = textName.text.toString()
        var surname = textSurname.text.toString()

        val docRef = db.collection("customers").document(viewModelShared.userId())
        docRef.update("name",name, "surname",surname)
            .addOnSuccessListener { Log.d("updateUserOK", "Usuario actualizado con apellido: $surname")
                                    Snackbar.make(v,"Usuario actualizado con exito", Snackbar.LENGTH_SHORT).show()
                                    //viewModelShared.getUserInfo() = user
            }
            .addOnFailureListener { e -> Log.d("updateUserNotOK", "get failed with ", e) }

    }

}