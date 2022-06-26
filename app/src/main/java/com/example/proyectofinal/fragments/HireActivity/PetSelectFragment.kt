package com.example.proyectofinal.fragments.HireActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Pet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PetSelectFragment : Fragment() {

    lateinit var v:View
    lateinit var spMeses : Spinner
    var db = Firebase.firestore

    //var listaMeses = listOf("")
    var petsList = listOf("hola", "chau")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        petsBD()
        v =  inflater.inflate(R.layout.fragment_pet_select, container, false)

        spMeses = v.findViewById(R.id.pet_select_spinner
        )
        setHasOptionsMenu(true)
        return v
    }

    override fun onStart() {
        super.onStart()
        populateSpinner(spMeses,petsList,requireContext())

        spMeses.setSelection(0, false)
        spMeses.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Snackbar.make(v, petsList[position], Snackbar.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = when(item.itemId) {

            //R.id.action_add -> Snackbar.make(v, "add", Snackbar.LENGTH_SHORT).show()

            //R.id.action_fav -> Snackbar.make(v, "fav", Snackbar.LENGTH_SHORT).show()

            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }


    private fun petsBD() {

        val original = listOf("orange", "apple")
        val other = listOf("banana", "strawberry")
        val newList = original + other // [orange, apple, banana, strawberry]

        db.collection("pets")
            .whereEqualTo("idOwner", userId())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (h in document) {
                        petsList = original
                        //petsList = petsList.plus
                        //petsList.add(h.data["name"] as String)
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("fallo", "Error getting documents: ", exception)
            }
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