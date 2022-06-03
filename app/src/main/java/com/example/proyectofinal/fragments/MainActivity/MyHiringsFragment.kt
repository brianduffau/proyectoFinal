package com.example.proyectofinal.fragments.MainActivity

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.HiringsFragment

class MyHiringsFragment : Fragment() {

    companion object {
        fun newInstance() = HiringsFragment()
    }

    private lateinit var viewModel: HiringsFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hirings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HiringsFragment::class.java)
        // TODO: Use the ViewModel
    }

}