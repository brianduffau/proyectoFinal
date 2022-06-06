package com.example.proyectofinal.fragments.recyclerViews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.adapters.HireAdapter
import com.example.proyectofinal.adapters.PetAdapter
import com.example.proyectofinal.entities.Hiring
import com.example.proyectofinal.viewmodels.HiringsFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HiringsFragment : Fragment() {

    private lateinit var viewModel: HiringsFragment
    lateinit var v: View
    lateinit var recHires : RecyclerView
    lateinit var adapter: HireAdapter
    var db = Firebase.firestore
    var hiresList : ArrayList<Hiring> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_hirings, container, false)

        recHires = v.findViewById(R.id.recHirings)
        // ESTO ACA O EN EL ONSTART
        recHires.setHasFixedSize(true)
        recHires.layoutManager = LinearLayoutManager(context)

        return v
    }

    override fun onStart() {
        super.onStart()

        hireBD()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HiringsFragment::class.java)
        // TODO: Use the ViewModel
    }

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

    fun hireBD () {
        db.collection("hirings")
            .whereEqualTo("id_customer", userId())
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    Log.i("ifHirings", "entro")
                    for (h in snapshot) {
                        hiresList.add(h.toObject())
                        // EL TEMA ES QUE ACA DEBERIA AGREGAR DISTINTO A LO QUE ESTA EN LA CLASE, SON LOS NOMBRES, NO LOS ID. LA BUSQUEDA LA HAGO ACA MISMO?
                    }
                    // YO TENDRIA QUE BUSCAR ESE ID DE CADA UNO EN LA COLECCION DE PROFESIONALES PARA VER EL NOMBRE
                    adapter = HireAdapter(requireContext(),hiresList){ position->
                        Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
                        Log.i("entro al for y adapter", "MASCOTAS: $hiresList")
                    }
                    recHires.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

    }


}