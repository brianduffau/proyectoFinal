package com.example.proyectofinal.fragments.MainActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.viewmodels.AddPetViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


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

    private var imageUri: Uri? = null
    private lateinit var photoPet: ImageView
    private lateinit var addPhoto: Button
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var petId: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_add_pet, container, false)

        // FIREBASE STORAGE
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference


        backButton = v.findViewById(R.id.back_button_toolbar)
        toolbarText = v.findViewById(R.id.text_toolbar)
        setupToolbar()

        addPet = v.findViewById(R.id.add_pet)
        namePetAdd = v.findViewById(R.id.petNameAdd)
        agePetAdd = v.findViewById(R.id.petAgeAdd)
        photoPet = v.findViewById(R.id.petPhotoAdd)
        addPhoto = v.findViewById(R.id.addPet_photo)
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


        val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            var uri= it.data?.data!!
            photoPet.setImageURI(uri)
            imageUri = uri
        }

        addPhoto.setOnClickListener{pickPhoto(imageLauncher)}

        addPet.setOnClickListener{

            if(imageUri != null){
                addPetStorage()
                Snackbar.make(v,"Mascota agregada con exito", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(v,"Por favor agregue una imagen para la mascota", Snackbar.LENGTH_SHORT).show()
            }

        }

        return v
    }

    private fun addPetStorage() {

        val riversRef: StorageReference = storageReference.child("pet/${userId()}/${namePetAdd.text}")

        riversRef.putFile(imageUri!!)
            .addOnSuccessListener { document -> // Get a URL to the uploaded content
                val downloadUrl = riversRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    val photo : String = it.toString()
                    addPetDB(photo) }
                Log.d("imgPetOk", "Imagen mascota con ID dueño: ${userId()}")

            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                Snackbar.make(v, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                Log.w("falloImgPet", "Error getting documents: ", it)
            }
    }



    private fun addPetDB(photo: String) {
        val data = mapOf(
            "name" to namePetAdd.text.toString(),
            "idOwner" to userId(),
            "age" to Integer.parseInt(agePetAdd.text.toString()),
            "type" to typeSelec,
            "imgPet" to photo,
            )

        db.collection("pets")
            .add(data)
            .addOnSuccessListener { document ->
                //uploadPhoto()
                //petId = document.id
                Log.d("addPetOk", "Mascota con ID: ${document.id}")
                cleanInputs()

            }
            .addOnFailureListener { exception ->
                Log.w("falloAddPet", "Error getting documents: ", exception)
            }

    }

    private fun pickPhoto(imageLauncher: ActivityResultLauncher<Intent>) {
        val intent= Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        imageLauncher.launch(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddPetViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setupToolbar() {
        toolbarText.setText("Mis mascotas")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack(R.id.userProfileFragment, false)}
    }

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

    private fun cleanInputs() {
        namePetAdd.setText("")
        selecPet.clearCheck()
        agePetAdd.setText("")
        photoPet.setImageURI(null)
    }



    // LA OTRA OPCION PARA CARGAR Y CONSULTAR LAS FOTOS:

    private fun uploadPhoto() {

        val riversRef: StorageReference = storageReference.child("pet/${userId()}/${namePetAdd.text}")

        riversRef.putFile(imageUri!!)
            .addOnSuccessListener { document -> // Get a URL to the uploaded content
                val downloadUrl = riversRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    val photo : String = it.toString()
                    updatePhotoDB(photo) }
                Log.d("imgPetOk", "Imagen mascota con ID dueño: ${userId()}")

            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                Log.w("falloImgPet", "Error getting documents: ", it)
            }
    }

    private fun updatePhotoDB(photo : String) {
        // Actualizar y subirle el url de donde esta guardada la imagen de la mascota en el storage
        val data = mapOf(
            "imgPet" to photo
        )

        db.collection("pets")
            .document(petId)
            .update(data)
    }

    private fun addPet() {
        val data = mapOf(
            "name" to namePetAdd.text.toString(),
            "idOwner" to userId(),
            "age" to Integer.parseInt(agePetAdd.text.toString()),
            "type" to typeSelec,

        )
        db.collection("pets")
            .add(data)
            .addOnSuccessListener { document ->
                uploadPhoto()
                petId = document.id
                Log.d("addPetOk", "Mascota con ID: ${document.id}")

            }
            .addOnFailureListener { exception ->
                Log.w("falloAddPet", "Error getting documents: ", exception)
            }

    }

}