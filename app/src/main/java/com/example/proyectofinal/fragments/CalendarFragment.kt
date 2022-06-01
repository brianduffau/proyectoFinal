package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.CalendarViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel

    lateinit var v: View

    private lateinit var calendar : CalendarView
    private lateinit var toolbarText : TextView
    private lateinit var backButton : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_calendar, container, false)

        findViews()
        setupToolbar()

        return v
    }

    //override fun onStart() {
    //    super.onStart()
    //    val profId = ProfessionalProfileFragmentArgs.fromBundle(requireArguments()).profId
    //}

    private fun findViews() {
        calendar = v.findViewById(R.id.calendar)
        toolbarText = v.findViewById(R.id.text_toolbar)
        backButton = v.findViewById(R.id.back_button_toolbar)
    }

    private fun setupToolbar() {
        toolbarText.setText("Contratar a @professional")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        // TODO: Use the ViewModel
    }

}