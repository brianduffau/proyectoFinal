package com.example.proyectofinal.fragments.HireActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.HiringViewModel

class HiringFragment : Fragment() {

    companion object {
        fun newInstance() = HiringFragment()
    }

    private lateinit var viewModel: HiringViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hiring, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HiringViewModel::class.java)
        // TODO: Use the ViewModel
    }

}