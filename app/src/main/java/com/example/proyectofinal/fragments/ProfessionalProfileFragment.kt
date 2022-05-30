package com.example.proyectofinal.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.R
import com.example.proyectofinal.fragments.MainActivity.MapFragment
import com.example.proyectofinal.viewmodels.ProfessionalProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfessionalProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfessionalProfileFragment()
    }

    lateinit var v : View

    private lateinit var viewModel: ProfessionalProfileViewModel

    lateinit var professionalName : TextView
    lateinit var professionalType : TextView

    lateinit var hireButton : ImageView

    var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_professional_profile, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()
        val profId = ProfessionalProfileFragmentArgs.fromBundle(requireArguments()).profId
        Log.d("argumento", "professional ID: $profId")

        professionalName = v.findViewById(R.id.professionalName)
        professionalType = v.findViewById(R.id.professionalType)
        hireButton = v.findViewById(R.id.hireButton)

        getProfInfo(profId)

        /*
            hireButton.setOnClickListener(){
            val mBuilder = AlertDialog.Builder(activity,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
                .setTitle("Contratar")
                .setMessage("Profesional: ")
                .setMessage("Disponibilidad:")
            val mAlertDialog = mBuilder.create()
            mAlertDialog.show()
        }*/

        // TODO: VER BIEN COMO HACER EL PASO A PASO DE LA CONTRATACION

        hireButton.setOnClickListener() {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Fechas disponibles")
                    .build()

            datePicker.show(childFragmentManager, "tag");

        }

    }

    private fun getProfInfo(profId : String) {
        val docRef = db.collection("professionals").document(profId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("documentOK", "DocumentSnapshot data: ${document.id}")
                    professionalName.text = document.data?.get("name") as String
                    professionalType.text = document.data?.get("professionalType") as String
                } else {
                    Log.d("documentNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("documentNotOK", "get failed with ", exception)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}