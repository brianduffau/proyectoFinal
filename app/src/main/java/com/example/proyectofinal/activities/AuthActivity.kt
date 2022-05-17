package com.example.proyectofinal.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.NavHostFragment
import com.example.proyectofinal.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity() {

    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment

    private lateinit var signUpButton : Button
    private lateinit var loginButton : Button
    private lateinit var emailEditText : EditText
    private lateinit var passEditText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        signUpButton = findViewById(R.id.signUpButton)
        loginButton = findViewById(R.id.loginButton)
        emailEditText = findViewById(R.id.emailEditText)
        passEditText = findViewById(R.id.passEditText)

        setup()

    }

    private fun setup(){
        title = "Autenticación"

        signUpButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passEditText.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            showHome()
                        }else{
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
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error al autenticar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

}