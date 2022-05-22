package com.example.proyectofinal.fragments.MainActivity

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.MapViewModel
import com.google.android.material.snackbar.Snackbar

class MapFragment : Fragment() {

    private lateinit var viewModel: MapViewModel
    lateinit var v:View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_map, container, false)


        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        // TODO: Use the ViewModel

    }


}

