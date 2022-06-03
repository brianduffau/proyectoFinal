package com.example.proyectofinal.fragments.recyclerViews

import android.content.ContentValues
import android.content.ContentValues.TAG
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

    private lateinit var viewModel: PetsViewModel
    lateinit var v: View

    lateinit var btnAdd : FloatingActionButton
    lateinit var recPets : RecyclerView
    lateinit var adapter: PetAdapter

    var db = Firebase.firestore

    var petsList : ArrayList<Pet> = arrayListOf()//mutablelist?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pets, container, false)

        btnAdd = v.findViewById(R.id.btn_add)
        recPets = v.findViewById(R.id.recPets)

        //petsList = arrayListOf()



        return v
    }

    override fun onStart() {
        super.onStart()

        // INTENTO DE CARGAR LOS DATOS A VER SI FUNCIONA:
        //petsList.add(Pet("01","https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-premium%2Flabrador-marron-foto-estudio-cachorro_12887850.htm&psig=AOvVaw2j8-0smiLmq9GD9vaaVKAY&ust=1653766170442000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCMCm-pq1gPgCFQAAAAAdAAAAABAD","India",9,"Perro", "1234AACC"))
        //petsList.add(Pet("Malta","Perro",1,"https://www.google.com/url?sa=i&url=https%3A%2F%2Fes.123rf.com%2Fimagenes-de-archivo%2Fperro_mestizo.html&psig=AOvVaw1UJKwhy7PGxgimszqB8LaB&ust=1653766045548000&source=images&cd=vfe&ved=0CAwQjRxqFwoTCMi5i-C0gPgCFQAAAAAdAAAAABAD", "1234AACC"))

        db.collection("pets")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    Log.i("if", "entro")
                    for (p in snapshot) {
                        // logica para que sea de ese dueño
                        // busco en el array de las mascotas del dueño y despues me fijo que sean el mismo id de la tabla de pets
                        // si son el mismo lo agrego a la petsList
                        petsList.add(p.toObject())
                    }
                        adapter = PetAdapter(requireContext(),petsList){ position->
                            Snackbar.make(v,position.toString(), Snackbar.LENGTH_SHORT).show()
                            // esto se ejecuta cuando hace click


                        Log.i("entro al for", "MASCOTAAS $petsList")
                    }
                    recPets.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }

        recPets.setHasFixedSize(true)
        recPets.layoutManager = LinearLayoutManager(context)






    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PetsViewModel::class.java)
        // TODO: Use the ViewModel
    }




}