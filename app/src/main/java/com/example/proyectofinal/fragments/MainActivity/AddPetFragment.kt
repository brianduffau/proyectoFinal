package com.example.proyectofinal.fragments.MainActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.AddPetViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddPetFragment : Fragment() {



    private lateinit var viewModel: AddPetViewModel
    lateinit var v: View
    var db = Firebase.firestore

    private lateinit var toolbarText : TextView
    private lateinit var backButton : ImageView

    lateinit var addPet : Button
    lateinit var namePetAdd : TextView
    lateinit var agePetAdd : TextView
    lateinit var selecPet: RadioGroup
    lateinit var dog: RadioButton
    lateinit var cat: RadioButton
    lateinit var typeSelec : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_add_pet, container, false)


        backButton = v.findViewById(R.id.back_button_toolbar)
        toolbarText = v.findViewById(R.id.text_toolbar)
        setupToolbar()

        addPet = v.findViewById(R.id.add_pet)
        namePetAdd = v.findViewById(R.id.petNameAdd)
        agePetAdd = v.findViewById(R.id.petAgeAdd)

        selecPet = v.findViewById(R.id.grupoSelecMascota)
        dog = v.findViewById(R.id.dog)
        cat = v.findViewById(R.id.cat)

        selecPet.clearCheck()

        selecPet.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.dog) {
                typeSelec = dog.text.toString()
            } else if (checkedId == R.id.cat) {
                typeSelec = cat.text.toString()
            }
            Log.d("petSelecc", "Selecciono:${typeSelec}")
        }



        addPet.setOnClickListener{addPet()
            Snackbar.make(v,"Mascota agregada con exito", Snackbar.LENGTH_SHORT).show()}

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddPetViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setupToolbar() {
        toolbarText.setText("Mis mascotas")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }

    private fun addPet() {
        val data = mapOf(
            "name" to namePetAdd.text.toString(),
            "idOwner" to userId(),
            "age" to Integer.parseInt(agePetAdd.text.toString()),
            "type" to typeSelec

        )
        db.collection("pets")
            .add(data)
            .addOnSuccessListener { document ->
                Log.d("addPetOk", "Mascota con ID: ${document.id}")
            }
            .addOnFailureListener { exception ->
                Log.w("falloAddPet", "Error getting documents: ", exception)
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

}