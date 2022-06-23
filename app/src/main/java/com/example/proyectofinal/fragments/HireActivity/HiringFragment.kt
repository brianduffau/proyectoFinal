package com.example.proyectofinal.fragments.HireActivity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.HireActivity
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.viewmodels.HiringViewModel
import java.util.*

class HiringFragment : Fragment() {

    private lateinit var v: View


    private lateinit var hiringInfo : TextView
    private lateinit var msg : TextView
    private lateinit var homeButton : ImageView

    private lateinit var professional : Professional
    private lateinit var hireStartDate : Calendar

    private lateinit var viewModel: HiringViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_hiring, container, false)

        professional = (activity as HireActivity).professional
        hireStartDate = (activity as HireActivity).hireStartDate

        setupViews()

        return v
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        hiringInfo = v.findViewById(R.id.hiring_info)
        msg = v.findViewById(R.id.hiring_msg_2)
        homeButton = v.findViewById(R.id.hiring_home_button)

        hiringInfo.text = "${professional.name} pasará por tu mascota el" +
                " ${hireStartDate.get(Calendar.DAY_OF_MONTH)}/${hireStartDate.get(Calendar.MONTH)}/${hireStartDate.get(Calendar.YEAR)}" +
                ", a las ${hireStartDate.get(Calendar.HOUR_OF_DAY)}:${hireStartDate.get(Calendar.MINUTE)}0"
        msg.text = "No olvides calificar a ${professional.name} cuando concluya el servicio"
        homeButton.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.actionHireToMain)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HiringViewModel::class.java)
        // TODO: Use the ViewModel
    }

}