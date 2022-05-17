package com.example.proyectofinal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.MyHiresViewModel

class MyHiresFragment : Fragment() {

    companion object {
        fun newInstance() = MyHiresFragment()
    }

    private lateinit var viewModel: MyHiresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_hires, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyHiresViewModel::class.java)
        // TODO: Use the ViewModel
    }

}