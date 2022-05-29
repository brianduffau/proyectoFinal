package com.example.proyectofinal.fragments.AuthActivity

import android.app.AlertDialog
import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    lateinit var v:View
    private lateinit var viewModel: RegisterViewModel
    private val db = Firebase.firestore

    private lateinit var registerButton: Button
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passInput: EditText
    private lateinit var passInput2: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        findViews()
        setupRegister()

        return v

    }

    private fun findViews() {
        registerButton = v.findViewById(R.id.registerButton)
        nameInput = v.findViewById(R.id.nameRegister)
        surnameInput = v.findViewById(R.id.surnameRegister)
        emailInput = v.findViewById(R.id.emailRegister)
        passInput = v.findViewById(R.id.passRegister)
        passInput2 = v.findViewById(R.id.passRegister2)

    }

    private fun setupRegister() {

        registerButton.setOnClickListener{
            if(checkFormNotEmpty()){
                if(doubleCheckPass()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailInput.text.toString(), passInput.text.toString())
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                saveUser()
                                //alert ok and navigate to login
                                accountCreatedAlert()
                            }else{
                                val toast = Toast.makeText(requireContext(), "El usuario ya existe", Toast.LENGTH_LONG)
                                toast.show()
                            }
                        }
                }else{
                    val toast = Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG)
                    toast.show()
                }
            }else{
                val toast = Toast.makeText(requireContext(), "Campos incompletos", Toast.LENGTH_LONG)
                toast.show()

            }
        }
    }

    private fun saveUser() {
        val user = hashMapOf(
            "name" to nameInput.text.toString(),
            "surname" to nameInput.text.toString(),
            "email" to nameInput.text.toString(),
        )
        db.collection("customers")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Usuario agregado con id : ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }


    private fun checkFormNotEmpty(): Boolean {
        return (nameInput.text.isNotEmpty()
                && surnameInput.text.isNotEmpty()
                && emailInput.text.isNotEmpty()
                && passInput.text.isNotEmpty()
                && passInput2.text.isNotEmpty())
    }

    private fun doubleCheckPass(): Boolean {
        return passInput.text.toString() == passInput2.text.toString()
    }

    private fun accountCreatedAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Felicitaciones!")
        builder.setMessage("Estás registrado en Mascoteando")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }


}