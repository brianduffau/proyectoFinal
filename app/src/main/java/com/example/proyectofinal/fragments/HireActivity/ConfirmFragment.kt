package com.example.proyectofinal.fragments.HireActivity

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.HireActivity
import com.example.proyectofinal.entities.Hiring
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.viewmodels.ConfirmViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ConfirmFragment : Fragment() {

    private lateinit var viewModel: ConfirmViewModel
    private lateinit var v : View

    private val db = Firebase.firestore

    private lateinit var professionalName : TextView
    //private lateinit var professionalImg : ImageView
    private lateinit var professionalType : TextView
    //private lateinit var rating : RatingBar
    private lateinit var confirm_msg : TextView

    private lateinit var professional : Professional
    private lateinit var hireStartDate : Calendar
    private lateinit var hireEndDate : Calendar
    private lateinit var confirmHireButton : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_confirm, container, false)

        professional = (activity as HireActivity).professional
        hireStartDate = (activity as HireActivity).hireStartDate
        hireEndDate = (activity as HireActivity).hireEndDate

        setUpViews()

        return v
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews() {
        professionalName = v.findViewById(R.id.confirm_professional_name)
        //professionalImg = v.findViewById(R.id.confirm_professional_img)
        professionalType = v.findViewById(R.id.confirm_professional_type)
        confirm_msg = v.findViewById(R.id.confirm_service_info)
        confirmHireButton = v.findViewById(R.id.confirm_hire_button)

        professionalName.text = professional.name
        professionalType.text = professional.professionalType
        confirm_msg.text = "${professional.name} pasará a buscar a tu mascota el ${hireStartDate.get(Calendar.DAY_OF_MONTH)}/${hireStartDate.get(Calendar.MONTH)}/${hireStartDate.get(Calendar.YEAR)}, a las"
        confirmHireButton.setOnClickListener{
            createHiring()
            Navigation.findNavController(v).navigate(R.id.actionConfirmToHire)
        }

    }

    private fun createHiring() {
        val customerId = Firebase.auth.currentUser?.uid
        val hiring = Hiring(customerId ?: "" ,professional.id, Timestamp(hireStartDate.time), Timestamp(hireEndDate.time))

        db.collection("hirings")
            .add(hiring)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Contratación agregada con id : ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmViewModel::class.java)
        // TODO: Use the ViewModel
    }

}