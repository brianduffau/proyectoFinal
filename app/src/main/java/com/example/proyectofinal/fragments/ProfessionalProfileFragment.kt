package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.ProfessionalProfileViewModel

class ProfessionalProfileFragment : Fragment() {

    private lateinit var viewModel: ProfessionalProfileViewModel

    lateinit var v: View
    private lateinit var profileImage : ImageView
    private lateinit var professionalType : TextView
    private lateinit var workQuantity : TextView
    private lateinit var rating : RatingBar
    private lateinit var recReviews : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_professional_profile, container, false)
        findViews()
        return v
    }

    private fun findViews() {
        profileImage = v.findViewById(R.id.professional_profile_image)
        professionalType = v.findViewById(R.id.professional_profile_type)
        workQuantity = v.findViewById(R.id.professional_work_qty)
        rating = v.findViewById(R.id.professional_profile_rating)
        recReviews = v.findViewById(R.id.professional_profile_rec_reviews)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}