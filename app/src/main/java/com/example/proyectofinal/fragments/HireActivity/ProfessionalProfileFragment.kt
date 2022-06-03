package com.example.proyectofinal.fragments.HireActivity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.HireActivity
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.viewmodels.ProfessionalProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class ProfessionalProfileFragment : Fragment(){

    private lateinit var viewModel: ProfessionalProfileViewModel

    lateinit var v: View

    private lateinit var professional : Professional
    private lateinit var profileImage : ImageView
    private lateinit var professionalType : TextView
    private lateinit var workQuantity : TextView
    private lateinit var rating : RatingBar
    private lateinit var recReviews : RecyclerView
    private lateinit var hireButton : LinearLayout
    private lateinit var professionalName : TextView

    private lateinit var calendar: Calendar
    private lateinit var date: Date
    private var hour : Int = 0
    private var minute : Int = 0
    


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_professional_profile, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()
        professional = (activity as HireActivity).professional
        calendar = Calendar.getInstance()
        setUpViews()
    }


    private fun setUpViews() {
        professionalName = v.findViewById(R.id.professional_profile_name)
        profileImage = v.findViewById(R.id.professional_profile_image)
        professionalType = v.findViewById(R.id.professional_profile_type)
        workQuantity = v.findViewById(R.id.professional_work_qty)
        rating = v.findViewById(R.id.professional_profile_rating)
        recReviews = v.findViewById(R.id.professional_profile_rec_reviews)
        hireButton = v.findViewById(R.id.hire_professional)

        professionalName.text = professional.name
        professionalType.text = professional.professionalType


        setupHireButton()

    }

    private fun setupHireButton() {

        hireButton.setOnClickListener {
            if (professional.professionalType == "Paseador"){
                datePicker()
            } else {
                dateRangePicker()
            }

        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun datePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccione fecha para el paseo")
                .build()
        datePicker.show(parentFragmentManager, null);
        datePicker.addOnPositiveButtonClickListener {
            calendar.time = Date(it)
            Log.d(TAG, "datePicker: ${calendar.time}")
            timePicker()
        }

    }

    private fun timePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setTitleText("¿A qué hora querés que pase ${professional.name}  ?")
                .build()
        timePicker.show(parentFragmentManager, null)
        timePicker.addOnPositiveButtonClickListener{
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)

            Log.d(TAG, "datePicker: ${calendar.time}")

        }
    }


    private fun dateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Seleccione ")
                .build()
        dateRangePicker.show(parentFragmentManager, null);
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}