package com.example.proyectofinal.viewmodels

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.entities.Customer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*

class UserProfileViewModel : ViewModel() {
    private lateinit var photo: String
    private val db = Firebase.firestore

    fun userId (): String {
        val user = Firebase.auth.currentUser
        var id : String = ""
        if (user != null) {
            id = user.uid
        }
        return id
    }

    fun getUserInfo(name: TextView, surname: TextView, photo : ImageView) {
        val docRef = db.collection("customers").document(userId())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("userOK", "User id: ${userId()} y time: ${Calendar.getInstance().time}")
                    name.text = document.data?.get("name") as String
                    surname.text = document.data?.get("surname") as String
                    Log.d("userImgOK", "Imagen URL: ${document.data?.get("img") as String}")
                    if(document.data?.get("img") != ""){
                        Picasso.get().load(document.data?.get("img") as String).fit().centerCrop().into(photo)
                    }

                } else {
                    Log.d("userNotFound", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("userNotOK", "get failed with ", exception)
            }
    }

    //UPDATE USER PROFILE FRAGMENT:

    fun addPhotoStorage(v: View, imageUri : Uri, image : ImageView) {

        val riversRef: StorageReference = FirebaseStorage.getInstance().reference.child("user/${userId()}/${Calendar.getInstance().time}")

        riversRef.putFile(imageUri)
            .addOnSuccessListener { document -> // Get a URL to the uploaded content
                val downloadUrl = riversRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    photo = it.toString()
                    updateImg(v, photo, image)
                }
                Log.d("imgUserOk", "Imagen usuario con ID: ${userId()}")

            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                Snackbar.make(v, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                Log.w("falloImgUser", "Error getting documents: ", it)
            }
    }

    fun updateImg(v: View, photo: String, image: ImageView) {
        Picasso.get().load(photo).fit().centerCrop().into(image)

        val docRef = db.collection("customers").document(userId())
        docRef.update("img", photo)
            .addOnSuccessListener { Log.d("updateUserOK", "Img actualizada: $photo")
                Snackbar.make(v,"Imagen actualizado con exito", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.d("updateUserNotOK", "get failed with ", e) }
    }


    fun updateUser(v: View, name: EditText, surname: EditText, image : ImageView, uri: Uri) {

        var nameUp = name.text.toString()
        var surnameUp = surname.text.toString()
        //Picasso.get().load(photo).fit().centerCrop().into(image)

        val docRef = db.collection("customers").document(userId())
        docRef.update("name",nameUp, "surname",surnameUp, "img", photo)
            .addOnSuccessListener { Log.d("updateUserOK", "Usuario actualizado con apellido: $surname")
                Snackbar.make(v,"Usuario actualizado con exito", Snackbar.LENGTH_SHORT).show()
                if (uri != null) {
                    addPhotoStorage(v,uri,image)
                }
            }
            .addOnFailureListener { e -> Log.d("updateUserNotOK", "get failed with ", e) }

    }

}