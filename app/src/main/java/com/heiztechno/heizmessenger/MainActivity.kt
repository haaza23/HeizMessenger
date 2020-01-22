package com.heiztechno.heizmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

private lateinit var txtUser : EditText
private lateinit var txtMail : EditText
private lateinit var txtPass : EditText
private lateinit var btnRegistrar : Button
private lateinit var txtLogIn : TextView

private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtUser = findViewById(R.id.txtUsername)
        txtMail = findViewById(R.id.txtMail)
        txtPass = findViewById(R.id.txtPassword)
        btnRegistrar = findViewById(R.id.btnRegister)
        txtLogIn = findViewById(R.id.txtLogIn)

        btnRegistrar.setOnClickListener {
            registerUser()
        }

        txtLogIn.setOnClickListener {
            Log.d("mainActivity", "mostrar pantalla log in")

            //Llamar a la actividad de Log In
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }


    private fun registerUser(){
        val mail = txtMail.text.toString()
        val contrasenia = txtPass.text.toString()

        if(mail.isEmpty() || contrasenia.isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("mainActivity", "El mail es: " + mail)
        Log.d("mainActivity", "La contraseña es: $contrasenia")

        //Conectar con Firebase
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(mail, contrasenia)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Firebase", "Se creo correctamente el uid: ${it?.result?.user?.uid}")
                    val user = auth.currentUser
                    //updateUI
                }else{
                    Toast.makeText(baseContext, "Error en la autenticacion",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "No se pudo crear el usuario: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("Firebase", "No se pudo crear el usuario: ${it.message}")
            }
    }
}
