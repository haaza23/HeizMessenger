package com.heiztechno.heizmessenger

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.heiztechno.heizmessenger.modules.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


private lateinit var textUser : EditText
private lateinit var textMail : EditText
private lateinit var textPass : EditText
private lateinit var btnRegistrar : Button
private lateinit var textLogIn : TextView
private lateinit var btnImagen : Button

var imageURI : Uri? = null

private lateinit var auth: FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        connectId()

        btnRegistrar.setOnClickListener {
            registerUser()
            try {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: Exception) { // TODO: handle exception
            }
        }

        txtLogIn.setOnClickListener {

            //Llamar a la actividad de Log In
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        btnImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            imageURI = data.data

            //val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
            //val bitmap = ImageDecoder.decodeBitmap(source)
            //Es el metodo que se deberia usar pero me da error y crashea la app

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageURI)
            imgSelected.setImageBitmap(bitmap)
            btnImage.alpha = 0f             //bring to back

            //val bitmapDrawable = BitmapDrawable(this.resources,bitmap)
            //btnImagen.setBackground(bitmapDrawable)
        }
    }

    private fun connectId(){
        textUser = findViewById(R.id.txtUsername)
        textMail = findViewById(R.id.txtMail)
        textPass = findViewById(R.id.txtPassword)
        btnRegistrar = findViewById(R.id.btnRegister)
        textLogIn = findViewById(R.id.txtLogIn)
        btnImagen = findViewById(R.id.btnImage)
    }

    private fun registerUser(){
        val mail = textMail.text.toString()
        val contrasenia = textPass.text.toString()

        if(mail.isEmpty() || contrasenia.isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "El mail es: " + mail)
        Log.d("RegisterActivity", "La contraseña es: $contrasenia")

        //Conectar con Firebase
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(mail, contrasenia)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Firebase", "Se creo correctamente el uid: ${it?.result?.user?.uid}")
                    val user = auth.currentUser
                    uploadImageToFirebase()
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

    private fun uploadImageToFirebase(){
        if(imageURI == null) return

        val filename = UUID.randomUUID().toString()                            //ID unico
        val storage = FirebaseStorage.getInstance().getReference("/images/$filename")

        storage.putFile(imageURI!!)                 //!! asegura que no es null por eso arriba lo verifico y si no corto el flujo
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Creado exitosamente: ${it.metadata?.path}")

                storage.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString())
                }
            }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            textUser.text.toString(),
            profileImageUrl
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Se guardo el usuario en la base de datos")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}
