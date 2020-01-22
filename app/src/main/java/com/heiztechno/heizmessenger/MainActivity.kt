package com.heiztechno.heizmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

private lateinit var txtUser : EditText
private lateinit var txtMail : EditText
private lateinit var txtPass : EditText
private lateinit var btnRegistrar : Button
private lateinit var txtLogIn : TextView

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
            val mail = txtMail.text.toString()
            val contrasenia = txtPass.text.toString()

            Log.d("mainActivity", "El mail es: " + mail)
            Log.d("mainActivity", "La contraseña es: $contrasenia")
        }

        txtLogIn.setOnClickListener {
            Log.d("mainActivity", "mostrar pantalla log in")

            //Llamar a la actividad de Log In
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}
