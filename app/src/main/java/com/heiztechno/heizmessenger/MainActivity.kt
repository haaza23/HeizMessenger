package com.heiztechno.heizmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText

private lateinit var txtUser : EditText
private lateinit var txtMail : EditText
private lateinit var txtPass : EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtUser = findViewById(R.id.txtUsername)
        txtMail = findViewById(R.id.txtMail)
        txtPass = findViewById(R.id.txtPassword)

        Log.d("mainActivity", "El mail es: " + txtMail)
        Log.d("mainActivity", "La contraseña es $txtPass")
    }
}
