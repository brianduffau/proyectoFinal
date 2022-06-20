package com.example.proyectofinal.fragments.HireActivity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.HireActivity
import com.example.proyectofinal.adapters.ReviewAdapter
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.entities.Review
import com.example.proyectofinal.viewmodels.ProfessionalProfileViewModel
import com.google.android.material.datepicker.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.math.abs


class ProfessionalProfileFragment : Fragment(){

    private lateinit var viewModel: ProfessionalProfileViewModel

    private lateinit var v: View

    private val db = Firebase.firestore

    private lateinit var professional : Professional
    private lateinit var profileImage : ImageView
    private lateinit var professionalType : TextView
    private lateinit var workQuantity : TextView
    private lateinit var rating : RatingBar
    private lateinit var hireButton : LinearLayout
    private lateinit var professionalName : TextView

    private lateinit var hireStartDate: Calendar
    private lateinit var hireEndDate: Calendar

    lateinit var recReviews : RecyclerView
    lateinit var adapter: ReviewAdapter
    var reviewsList : ArrayList<Review> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_professional_profile, container, false)


        recReviews = v.findViewById(R.id.recReviews)
        // ESTO ACA O EN EL ONSTART
        recReviews.setHasFixedSize(true)
        recReviews.layoutManager = LinearLayoutManager(context)

        return v
    }

    override fun onStart() {
        super.onStart()

        professional = (activity as HireActivity).professional
        hireStartDate = getInstance()
        hireEndDate = getInstance()

        getReviews()

        setUpViews()

    }


    private fun setUpViews() {
        professionalName = v.findViewById(R.id.professional_profile_name)
        profileImage = v.findViewById(R.id.professional_profile_image)
        professionalType = v.findViewById(R.id.professional_profile_type)
        workQuantity = v.findViewById(R.id.professional_work_qty)
        rating = v.findViewById(R.id.professional_profile_rating)
        hireButton = v.findViewById(R.id.hire_professional)

        professionalName.text = professional.name
        professionalType.text = professional.professionalType
        Picasso.get().load(professional.img).fit().centerCrop().into(profileImage)


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

    private fun calendarConstraints() : CalendarConstraints {

        val dateValidatorMin = DateValidatorPointForward.from(hireStartDate.timeInMillis)
        //val dateValidatorMax = DateValidatorPointBackward.before()

        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
        listValidators.apply {
            add(dateValidatorMin)
            //add(dateValidatorMax)
        }
        val validators = CompositeDateValidator.allOf(listValidators)

        return CalendarConstraints.Builder()
            .setValidator(validators)
            .build()
    }

    @SuppressLint("SimpleDateFormat")
    private fun datePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setCalendarConstraints((calendarConstraints()))
                .setTitleText("Seleccione un día para el paseo")
                .build()

        datePicker.show(parentFragmentManager, null)
        datePicker.addOnPositiveButtonClickListener {
            val localTimeMilliseconds = it + abs(hireStartDate.timeZone.rawOffset)

            hireStartDate.time = Date(localTimeMilliseconds)
            hireEndDate.time = Date(localTimeMilliseconds)

            startTimePicker()

        }
    }

    private fun startTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setTitleText("¿A qué hora querés que pase ${professional.name}?")
                .build()
        timePicker.show(parentFragmentManager, null)
        timePicker.addOnPositiveButtonClickListener{
            if(timePicker.hour in 10..19){
                hireStartDate.set(HOUR_OF_DAY, timePicker.hour)
                hireStartDate.set(MINUTE, timePicker.minute)

                Log.d(TAG, "datePicker: ${hireStartDate.time}")

                (activity as HireActivity).hireStartDate = hireStartDate //hacer en view-model

                endDatePicker()
            }else{
                Snackbar.make(v,"${professional.name} trabaja de 9hs a 20hs, ingrese otro horario",Snackbar.LENGTH_LONG).show()
            }


        }
    }

    private fun endDatePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setTitleText("¿Hasta qué hora?")
                .build()
        timePicker.show(parentFragmentManager, null)
        timePicker.addOnPositiveButtonClickListener{
            hireEndDate.set(HOUR_OF_DAY, timePicker.hour)
            hireEndDate.set(MINUTE, timePicker.minute)

            Log.d(TAG, "datePicker: ${hireEndDate.time}")


            (activity as HireActivity).hireEndDate = hireEndDate //hacer en view model

            disponibilityCheck()
            //Navigation.findNavController(v).navigate(R.id.actionProfessionalToConfirm)

        }
    }

    private fun disponibilityCheck() {
        var cantidadDeContrataciones = 0;

        db.collection("hirings")
            .whereEqualTo("id_professional", professional.id)
            .whereEqualTo("startDate", Timestamp(hireStartDate.time))
            .whereEqualTo("endDate", Timestamp(hireEndDate.time))
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (h in snapshot) {
                        cantidadDeContrataciones++;
                    }
                }

                if(cantidadDeContrataciones < professional.petQty){
                    Navigation.findNavController(v).navigate(R.id.actionProfessionalToConfirm)
                }else{
                    Snackbar.make(v, "${professional.name} no tiene disponible ese horario, por favor seleccione otro horario", Snackbar.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }
    }

    private fun dateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker().setCalendarConstraints(calendarConstraints())
                .setTitleText("Seleccione ")
                .build()
        dateRangePicker.show(parentFragmentManager, null)

        dateRangePicker.addOnPositiveButtonClickListener {
            val startDateMilliseconds = dateRangePicker.selection?.first
            val endDateMilliseconds = dateRangePicker.selection?.second

            if(startDateMilliseconds != null && endDateMilliseconds != null){
                hireStartDate.time = Date(startDateMilliseconds)
                hireEndDate.time = Date(endDateMilliseconds)

                (activity as HireActivity).hireStartDate = hireStartDate
                (activity as HireActivity).hireEndDate = hireEndDate
            }

            disponibilityCheck()
            //Navigation.findNavController(v).navigate(R.id.actionProfessionalToConfirm)

            Log.d(TAG, "dateRangePicker: ${hireStartDate.time} y ${hireEndDate.time}")
        }
    }

    private fun getReviews () {
        db.collection("reviews")
            .whereEqualTo("id_reviewed", professional.id.trimStart())
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (h in snapshot) {
                        reviewsList.add(h.toObject())
                    }
                    adapter = ReviewAdapter(requireContext(),reviewsList){position->
                        Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    recReviews.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}