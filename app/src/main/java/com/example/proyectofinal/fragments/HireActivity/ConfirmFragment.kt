package com.example.proyectofinal.fragments.HireActivity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.HireActivity
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.viewmodels.ConfirmViewModel
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

class ConfirmFragment : Fragment() {

    private lateinit var viewModel: ConfirmViewModel
    private lateinit var v : View

    private lateinit var professionalName : TextView
    //private lateinit var professionalImg : ImageView
    private lateinit var professionalType : TextView
    //private lateinit var rating : RatingBar
    private lateinit var confirm_msg : TextView

    private lateinit var professional : Professional
    private lateinit var hireStartDate : Calendar
    private lateinit var hireEndDate : Calendar


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

        professionalName.text = professional.name
        professionalType.text = professional.professionalType
        confirm_msg.text = "${professional.name} pasará a buscar a tu mascota el ${hireStartDate.get(Calendar.DAY_OF_MONTH)}"

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmViewModel::class.java)
        // TODO: Use the ViewModel
    }

}