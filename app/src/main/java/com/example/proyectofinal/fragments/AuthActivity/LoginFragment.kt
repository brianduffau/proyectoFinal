package com.example.proyectofinal.fragments.AuthActivity

import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
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
import com.example.proyectofinal.entities.Customer
import com.example.proyectofinal.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class LoginFragment : Fragment() {

    private val GOOGLE_SIGN_IN = 100

    lateinit var v: View
    private val db = Firebase.firestore
    private lateinit var viewModel: AuthViewModel

    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var googleButton: ImageButton
    private lateinit var backButton : ImageView
    private lateinit var toolbarText : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)
        findViews()
        setupToolbar()
        setupListeners()

        return v
    }

    private fun setupToolbar() {
        toolbarText.setText("Acceder con una cuenta")
        backButton.setOnClickListener{Navigation.findNavController(v).popBackStack()}
    }

    private fun findViews() {
        signUpButton = v.findViewById(R.id.register_button)
        loginButton = v.findViewById(R.id.login_button)
        emailEditText = v.findViewById(R.id.email_login)
        passEditText = v.findViewById(R.id.pass_login)
        googleButton = v.findViewById(R.id.google_button_login)
        toolbarText = v.findViewById(R.id.text_toolbar)
        backButton = v.findViewById(R.id.back_button_toolbar)
    }

    private fun setupListeners() {

        signUpButton.setOnClickListener{Navigation.findNavController(v).navigate(R.id.actionAuthToRegister)}

        loginButton.setOnClickListener {
            Navigation.findNavController(v).navigate(R.id.authToMain)
        }

        googleButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(requireActivity(), googleConf)

            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
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
                                Log.d(TAG, "onActivityResult: ${it.result.user?.photoUrl}")
                                Log.d(TAG, "onActivityResult: ${account.photoUrl}")

                                if (it.getResult().additionalUserInfo?.isNewUser!!) {
                                    saveUser(
                                        it.result.user?.uid ?: "",
                                        account.givenName ?: "",
                                        "",
                                        account.email ?: "",
                                        "",
                                    )
                                }
                                Navigation.findNavController(v).navigate(R.id.authToMain)
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


    private fun saveUser(id: String, name: String, surname: String, email: String, img: String) {
        val customer = Customer(id = id, name = name, surname = surname, email = email, img = img)

        db.collection("customers").document(id)
            .set(customer)
            .addOnSuccessListener { documentReference ->
                //addPhotoStorage(id)
                Log.d("saveUserOk", "Usuario agregado con id : $id")
            }
            .addOnFailureListener { e ->
                Log.w("saveUserNotOk", "Error adding document", e)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        // TODO: Use the ViewModel
    }


}