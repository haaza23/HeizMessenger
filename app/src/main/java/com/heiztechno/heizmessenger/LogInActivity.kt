package com.heiztechno.heizmessenger

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


private lateinit var txtMail : EditText
private lateinit var txtPass : EditText
private lateinit var btnLogIn : Button
private lateinit var txtRegister : TextView

private lateinit var auth: FirebaseAuth

class LogInActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtMail = findViewById(R.id.txtMail)
        txtPass = findViewById(R.id.txtPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        txtRegister = findViewById(R.id.txtRegister)

        btnLogIn.setOnClickListener {
            LoginUser()
            try {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) { // TODO: handle exception
            }
        }

        txtRegister.setOnClickListener {
            finish()
        }
    }

    private fun LoginUser(){
        val mail = txtMail.text.toString()
        val contrasenia = txtPass.text.toString()

        if(mail.isEmpty() || contrasenia.isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("loginActivity", "El mail es: " + mail)
        Log.d("loginActivity", "La contraseña es: $contrasenia")

        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(mail, contrasenia)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d("Firebase", "Se logeo correctamente el uid: ${it?.result?.user?.uid}")
                    val user = auth.currentUser
                    //updateUI
                }else{
                    Toast.makeText(baseContext, "Error en la autenticacion",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}