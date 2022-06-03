package com.example.proyectofinal.fragments.HireActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.ConfirmationViewModel
import com.google.firebase.firestore.DocumentSnapshot

class ConfirmationFragment : Fragment() {

    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var v : View
    private lateinit var professionalName : TextView
    private lateinit var professionalDoc : DocumentSnapshot

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_confirm, container, false)

        //setUpViews()

        return v
    }

    private fun setUpViews() {
        professionalName = v.findViewById(R.id.confirm_professional_name)
        professionalName.text = professionalDoc.data?.get("name").toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}