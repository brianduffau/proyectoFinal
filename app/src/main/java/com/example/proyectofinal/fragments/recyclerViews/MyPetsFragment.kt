package com.example.proyectofinal.fragments.recyclerViews

import android.content.ContentValues
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
import com.example.proyectofinal.adapters.PetAdapter
import com.example.proyectofinal.entities.Pet
import com.example.proyectofinal.viewmodels.PetsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MyPetsFragment : Fragment() {

    companion object {
        fun newInstance() = MyPetsFragment()
    }

    private lateinit var viewModel: PetsViewModel
    lateinit var v: View

    lateinit var btnAdd : FloatingActionButton
    lateinit var recPets : RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapterPet: PetAdapter

    lateinit var petsList : ArrayList<Pet>


    var db = Firebase.firestore

    override fun onStart() {
        super.onStart()

        db.collection("pets")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    for (p in snapshot) {
                        // logica para que sea de ese dueño
                        // busco en el array de las mascotas del dueño y despues me fijo que sean el mismo id de la tabla de pets
                        // si son el mismo lo agrego a la petsList
                        petsList.add(p.toObject())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        recPets.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recPets.layoutManager = LinearLayoutManager(context)
        adapterPet = PetAdapter(requireContext(),petsList){ position->
            Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
            // esto se ejecuta cuando hace click
        }
        recPets.adapter = adapterPet

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pets, container, false)

        btnAdd = v.findViewById(R.id.btn_add)
        recPets = v.findViewById(R.id.recPets)





        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetsViewModel::class.java)
        // TODO: Use the ViewModel
    }




}