package com.example.proyectofinal.fragments.HireActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Pet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PetSelectFragment : Fragment() {

    lateinit var v:View
    lateinit var spPets : Spinner
    var db = Firebase.firestore

    var petsList : ArrayList<String> = arrayListOf()
    var petName : String = ""
    lateinit var adapter : ArrayAdapter<String>
    lateinit var btnNext : Button

    private lateinit var backButton: ImageView
    private lateinit var toolbarText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v =  inflater.inflate(R.layout.fragment_pet_select, container, false)
        toolbarText = v.findViewById(R.id.text_toolbar)
        backButton = v.findViewById(R.id.back_button_toolbar)

        btnNext = v.findViewById(R.id.btn_pet_select)
        spPets = v.findViewById(R.id.pet_select_spinner)
        setHasOptionsMenu(true)

        spPets.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                petName = petsList[position]
                Snackbar.make(v, "La mascota seleccionada es " + petsList[position], Snackbar.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        btnNext.setOnClickListener{
            val action = PetSelectFragmentDirections.actionPetSelectToConfirm(petName)
            v.findNavController().navigate(action)

        }

        setupToolbar()
        return v
    }

    private fun setupToolbar() {
        toolbarText.setText("Seleccionar")
        backButton.setOnClickListener { Navigation.findNavController(v).popBackStack(R.id.professionalProfileFragment, false) }
    }


    override fun onStart() {
        super.onStart()
        petsBD()
        spPets.setSelection(0, false)
        /*

        populateSpinner(spPets,petsList,requireContext())


        spPets.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Snackbar.make(v, petsList[position], Snackbar.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

         */
    }


    private fun petsBD() {

        db.collection("pets")
            .whereEqualTo("idOwner", userId())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (h in document) {
                        petsList.add(h.data["name"] as String)
                    }
                adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, petsList)
                spPets.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }
    }

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }


    fun populateSpinner (spinner: Spinner, list : List<String>, context : Context)
    {
        //   val aa = ArrayAdapter( context!!, android.R.layout.simple_spinner_item, list)
        val aa = ArrayAdapter(context,android.R.layout.simple_spinner_item, list)

        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner.setAdapter(aa)
    }
}




