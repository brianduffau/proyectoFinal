package com.example.proyectofinal.fragments.AuthActivity

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.proyectofinal.R
import com.example.proyectofinal.activities.MapActivity
import com.example.proyectofinal.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthFragment : Fragment() {

    private val GOOGLE_SIGN_IN = 100

    private lateinit var viewModel: AuthViewModel

    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var googleButton: ImageButton
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButton = view.findViewById(R.id.signUpButton)
        loginButton = view.findViewById(R.id.loginButton)
        emailEditText = view.findViewById(R.id.emailEditText)
        passEditText = view.findViewById(R.id.passEditText)
        googleButton = view.findViewById(R.id.googleButton)

        setup()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setup() {

        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var email = emailEditText.text.toString()
                        saveUser(email ?: "")
                        showHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }

        loginButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),
                    passEditText.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showHome()
                    }else{
                        showAlert()
                    }
                }
            }
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

    private fun showAlert(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error al autenticar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        val homeIntent = Intent(activity, MapActivity::class.java)
        startActivity(homeIntent)
    }

    private fun saveUser(email: String){
        val user = hashMapOf(
            "mail" to email)
        db.collection("customers")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Usuario agregado con id : ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            //var email = emailEditText?.text?.toString()
                            //saveUser(email ?: "")
                            showHome()
                        }else{
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert()
            }

        }
    }






}