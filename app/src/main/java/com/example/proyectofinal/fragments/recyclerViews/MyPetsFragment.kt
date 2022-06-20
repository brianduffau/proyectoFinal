package com.example.proyectofinal.fragments.recyclerViews


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.adapters.PetAdapter
import com.example.proyectofinal.entities.Pet
import com.example.proyectofinal.viewmodels.PetsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MyPetsFragment : Fragment() {

    private lateinit var viewModel: PetsViewModel
    lateinit var v: View

    lateinit var btnAdd : FloatingActionButton
    lateinit var recPets : RecyclerView
    lateinit var adapter: PetAdapter

    private lateinit var backButton: ImageView
    private lateinit var toolbarText: TextView

    var db = Firebase.firestore

    var petsList : ArrayList<Pet> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pets, container, false)

        btnAdd = v.findViewById(R.id.btn_add)
        recPets = v.findViewById(R.id.recPets)

        toolbarText = v.findViewById(R.id.text_toolbar)
        backButton = v.findViewById(R.id.back_button_toolbar)

        // ESTO EN EL CREATED O EN ONSTART
        recPets.setHasFixedSize(true)
        recPets.layoutManager = LinearLayoutManager(context)


        btnAdd.setOnClickListener{ Navigation.findNavController(v).navigate(R.id.actionListPetsAddPet)}


        setupToolbar()
        return v
    }

    private fun setupToolbar() {
        toolbarText.setText("Mis mascotas")
        backButton.setOnClickListener { Navigation.findNavController(v).popBackStack() }
    }


    override fun onStart() {
        super.onStart()

        petsBD()

    }

    private fun petsBD() {
        // CHEQUEAR SI DESPUES DE AGREGAR UNA MASCOTA, APARECE REPETIDA LA QUE YA ESTABA...
        db.collection("pets")
            .whereEqualTo("idOwner", userId())
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    Log.i("if", "entro")
                    for (p in snapshot) {
                        petsList.add(p.toObject())
                    }
                    adapter = PetAdapter(requireContext(),petsList){ position->
                        Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
                        Log.i("entro al for y adapter", "MASCOTAS: $petsList")
                    }
                    recPets.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetsViewModel::class.java)
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


}