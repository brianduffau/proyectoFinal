package com.example.proyectofinal.fragments.MainActivity

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.UpdateUserProfileViewModel
import com.example.proyectofinal.viewmodels.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.net.URL
import java.util.*

class UpdateUserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateUserProfileFragment()
    }

    private lateinit var viewModel: UpdateUserProfileViewModel
    private lateinit var viewModelShared: UserProfileViewModel

    private lateinit var textName : EditText
    private lateinit var textSurname : EditText
    private lateinit var photoUser : ImageView
    private lateinit var buttonUpdate : Button
    private lateinit var buttonPhoto : Button
    //lateinit var user : Customer
    private val db = Firebase.firestore
    private lateinit var imageUri: Uri
    private lateinit var photo: String
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var toolbarText : TextView
    private lateinit var backButton : ImageView

    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_update_user_profile, container, false)

        backButton = v.findViewById(R.id.back_button_toolbar)
        toolbarText = v.findViewById(R.id.text_toolbar)
        setupToolbar()

        textName = v.findViewById(R.id.nameUserUp)
        textSurname = v.findViewById(R.id.surnameUserUp)
        photoUser = v.findViewById(R.id.imageUserUp)
        buttonPhoto = v.findViewById(R.id.updatePhoto)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            var uri= it.data?.data!!
            photoUser.setImageURI(uri)
            Picasso.get().load(uri).fit().centerCrop().into(photoUser)
            imageUri = uri
        }

        buttonPhoto.setOnClickListener{pickPhoto(imageLauncher)}


        buttonUpdate = v.findViewById(R.id.updateProf)
        buttonUpdate.setOnClickListener{ addPhotoStorage()}

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateUserProfileViewModel::class.java)
        viewModelShared = ViewModelProvider(requireActivity()).get(UserProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun pickPhoto(imageLauncher: ActivityResultLauncher<Intent>) {
        val intent= Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        imageLauncher.launch(intent)
    }

    private fun setupToolbar() {
        toolbarText.setText("Mi perfil")
        backButton.setOnClickListener{ Navigation.findNavController(v).popBackStack()}
    }

    override fun onStart() {
        super.onStart()
        getUserInfo()
        //user = viewModelShared.getUserInfo()!!
        //textName.setText(user.name)
        //textSurname.setText(user.surname)

    }

    private fun addPhotoStorage() {

        val riversRef: StorageReference = storageReference.child("user/${viewModelShared.userId()}/${Calendar.getInstance().time}")

        riversRef.putFile(imageUri)
            .addOnSuccessListener { document -> // Get a URL to the uploaded content
                val downloadUrl = riversRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    photo = it.toString()
                    updateUser(photo)
                }
                Log.d("imgUserOk", "Imagen usuario con ID: ${viewModelShared.userId()}")

            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                Snackbar.make(v, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                Log.w("falloImgUser", "Error getting documents: ", it)
            }
    }

    fun getUserInfo() {
        val docRef = db.collection("customers").document(viewModelShared.userId())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("userOK", "DocumentSnapshot data: ${document.id} y img url: ${document.data?.get("img") as String} ")
                    textName.setText(document.data?.get("name") as String)
                    textSurname.setText(document.data?.get("surname") as String)
                    Log.d("userImgUpOK", "Imagen URL: ${document.data?.get("img") as String}")
                    Picasso.get().load(document.data?.get("img") as String).fit().centerCrop().into(photoUser)
                } else {
                    Log.d("userNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("userNotOK", "get failed with ", exception)
            }
    }

    private fun updateUser(photo: String) {

        var name = textName.text.toString()
        var surname = textSurname.text.toString()
        Picasso.get().load(photo).fit().centerCrop().into(photoUser)

        val docRef = db.collection("customers").document(viewModelShared.userId())
        docRef.update("name",name, "surname",surname, "img", photo)
            .addOnSuccessListener { Log.d("updateUserOK", "Usuario actualizado con apellido: $surname")
                                    Snackbar.make(v,"Usuario actualizado con exito", Snackbar.LENGTH_SHORT).show()
                                    //viewModelShared.getUserInfo() = user
            }
            .addOnFailureListener { e -> Log.d("updateUserNotOK", "get failed with ", e) }

    }

}