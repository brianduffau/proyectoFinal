package com.example.proyectofinal.fragments.AuthActivity

import android.R.attr.path
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
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
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.RegisterViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class RegisterFragment : Fragment() {

    lateinit var v: View
    private lateinit var viewModel: RegisterViewModel
    private val db = Firebase.firestore

    private val GOOGLE_SIGN_IN = 100

    private lateinit var registerButton: Button
    private lateinit var googleRegisterButton: ImageButton
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passInput: EditText
    private lateinit var photoUser: ImageView
    private lateinit var addPhoto: Button
    private lateinit var imageUri: Uri
    private lateinit var passConfirmInput: EditText
    private lateinit var backButton: ImageView
    private lateinit var toolbarText: TextView
    private lateinit var photo: String

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register, container, false)

        findViews()
        setupToolbar()
        setupRegister()

        // FIREBASE STORAGE
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            var uri= it.data?.data!!
            photoUser.setImageURI(uri)
            imageUri = uri
        }

        addPhoto.setOnClickListener{pickPhoto(imageLauncher)}

        return v
    }

    fun pickPhoto(imageLauncher: ActivityResultLauncher<Intent>) {
        val intent= Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        imageLauncher.launch(intent)
    }

    private fun setupToolbar() {
        toolbarText.setText("Registrar una nueva cuenta")
        backButton.setOnClickListener { Navigation.findNavController(v).popBackStack() }
    }

    private fun findViews() {
        registerButton = v.findViewById(R.id.button_register)
        googleRegisterButton = v.findViewById(R.id.google_button_register)
        nameInput = v.findViewById(R.id.name_register)
        surnameInput = v.findViewById(R.id.surname_register)
        emailInput = v.findViewById(R.id.email_register)
        passInput = v.findViewById(R.id.pass_register)
        passConfirmInput = v.findViewById(R.id.pass_confirm_register)
        toolbarText = v.findViewById(R.id.text_toolbar)
        backButton = v.findViewById(R.id.back_button_toolbar)
        addPhoto = v.findViewById(R.id.addPhoto_register)
        photoUser = v.findViewById(R.id.image_register)
    }

    private fun addPhotoStorage(id : String) {

        val riversRef: StorageReference = storageReference.child("user/${id}/${Calendar.getInstance().time}")

        riversRef.putFile(imageUri)
            .addOnSuccessListener { document -> // Get a URL to the uploaded content
                val downloadUrl = riversRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    photo = it.toString()
                    savePhoto(photo, id)
                }
                Log.d("imgUserOk", "Imagen usuario con ID: ${id}")

                }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                Snackbar.make(v, "Error al cargar la imagen", Snackbar.LENGTH_SHORT).show()
                Log.w("falloImgUser", "Error getting documents: ", it)
                }
    }

    private fun savePhoto(photo: String, id: String) {
        val data = mapOf(
            "img" to photo
        )

        db.collection("customers")
            .document(id)
            .update(data)
    }

    private fun setupRegister() {

        registerButton.setOnClickListener {
            if (checkFormNotEmpty()) {
                if (doubleCheckPass()) {
                    if(checkShortPass()) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            emailInput.text.toString(),
                            passInput.text.toString()
                        )
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    saveUser(
                                        it.getResult().user?.uid ?: "",
                                        nameInput.text.toString(),
                                        surnameInput.text.toString(),
                                        emailInput.text.toString(),
                                        ""
                                    )
                                    accountCreatedAlert()
                                } else {
                                    Log.d(TAG, "setupRegister: $it")
                                    val toast = Toast.makeText(
                                        requireContext(),
                                        "El usuario ya existe",
                                        Toast.LENGTH_LONG
                                    )
                                    toast.show()
                                }
                            }
                    } else {
                        val toast = Toast.makeText(
                            requireContext(),
                            "La contraseña debe tener más de 6 caracteres",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        requireContext(),
                        "Las contraseñas no coinciden",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            } else {
                val toast =
                    Toast.makeText(requireContext(), "Campos incompletos", Toast.LENGTH_LONG)
                toast.show()

            }
        }

        googleRegisterButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(requireActivity(), googleConf)

            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (it.getResult().additionalUserInfo?.isNewUser!!) {
                                    saveUser(
                                        it.result.user?.uid ?: "",
                                        account.givenName ?: "",
                                        "",
                                        account.email ?: "",
                                        account.photoUrl.toString(),
                                    )
                                }
                                Navigation.findNavController(v).navigate(R.id.actionRegisterToMain)
                            } else {
                                showAlert()
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert()
            }

        }


    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error al autenticar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveUser(id: String, name: String, surname: String, email: String, img: String) {
        val customer = Customer(id = id, name = name, surname = surname, email = email, img = img)

        db.collection("customers").document(id)
            .set(customer)
            .addOnSuccessListener { documentReference ->
                addPhotoStorage(id)
                Log.d("saveUserOk", "Usuario agregado con id : $id")
            }
            .addOnFailureListener { e ->
                Log.w("saveUserNotOk", "Error adding document", e)
            }
    }


    private fun checkFormNotEmpty(): Boolean {
        return (nameInput.text.isNotEmpty()
                && surnameInput.text.isNotEmpty()
                && emailInput.text.isNotEmpty()
                && passInput.text.isNotEmpty()
                && passConfirmInput.text.isNotEmpty())
    }

    private fun doubleCheckPass(): Boolean {
        return passInput.text.toString() == passConfirmInput.text.toString()
    }

    private fun checkShortPass(): Boolean {
        return passInput.text.length > 5
    }

    private fun accountCreatedAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Felicitaciones!")
        builder.setMessage("Estás registrado en Mascoteando")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        cleanInputs()
    }

    private fun cleanInputs() {
        nameInput.setText("")
        surnameInput.setText("")
        emailInput.setText("")
        passInput.setText("")
        passConfirmInput.setText("")
        photoUser.setImageURI(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }




}