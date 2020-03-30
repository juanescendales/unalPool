package com.example.unalpool

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import com.example.unalpool.Models.User

class RegisterActivity : AppCompatActivity () {
    var selectedPhotoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Log.d("RegisterActivity","Hola1")
        button_register.setOnClickListener {
            crearRegistro()
        }
        cuenta_existe_textView_register.setOnClickListener {
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
        }
        button_photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // Se quiere atrapar la imagen que fue seleccionada por el usuario
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphoto_imageview_register.setImageBitmap(bitmap)
            button_photo.alpha = 0f
            Log.d("RegisterActivity", "Foto seleccionada satisfactoriamente: $selectedPhotoUri")
        }
    }

    private fun crearRegistro(){
        button_register.isClickable = false
        val email = email_editText_register.text.toString()
        val password = password_editText_register.text.toString()
        val phone = phone_editText_register.text.toString()
        Log.d("RegisterActivity","email: $email and password $password")
        if(email == "" || password == "" || phone == "") {
            Toast.makeText(baseContext, "Llena todos los campos",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(password.length < 6){
            Toast.makeText(baseContext, "Contraseña minimo de 6 caracteres",
                Toast.LENGTH_SHORT).show()
            button_register.isClickable = true
            return
        }
        if(password.length > 16){
            Toast.makeText(baseContext, "Contraseña maximo de 20 caracteres",
                Toast.LENGTH_SHORT).show()
            button_register.isClickable = true
            return
        }
        if(!("@unal.edu.co" in email)){
            Toast.makeText(baseContext, "Solo son validos correos de la universidad",
                Toast.LENGTH_SHORT).show()
            button_register.isClickable = true
            return
        }
        if(phone.length != 10){
            Toast.makeText(baseContext, "El telefono celular es de 11 digitos",
                Toast.LENGTH_SHORT).show()
            button_register.isClickable = true
            return
        }
        val name = email.removeSuffix("@unal.edu.co")
        Log.d("RegisterActivity", "Nombre del usuario : $name")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("RegisterActivity", "Entra a firebase")
                    Log.d("RegisterActivity", "Successfully created user with uid: ${it.result?.user?.uid}")
                    Log.d("RegisterActivity", "createUserWithEmail:success")
                    guardarUsuarioEnFirebase(name,email,phone)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("RegisterActivity", "createUserWithEmail:failure", it.exception)
                    Toast.makeText(baseContext, "Fallo en el proceso de registro: ${it.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                    button_register.isClickable = true
                }
            }
    }
    private fun guardarUsuarioEnFirebase(nombre: String,correo: String, telefono: String){
        Log.d("RegisterActivity", "valor de la URI: $selectedPhotoUri")
        if(selectedPhotoUri == null){
            guardarUsuarioBaseDeDatos(nombre,correo,"",telefono)
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Guardado de foto satisfactorio: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {task ->
                    val downLoadReference = task.toString()
                    Log.d("RegisterActivity","Guardado de foto satisfactorio: $downLoadReference")
                    guardarUsuarioBaseDeDatos(nombre,correo,downLoadReference,telefono)
                }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Error al guardar foto: ${it.message}")
                Toast.makeText(baseContext, "Error al guardar la imagen",
                    Toast.LENGTH_SHORT).show()
                button_register.isClickable = true
            }
    }
    private fun guardarUsuarioBaseDeDatos(nombre:String,correo:String,imagenUrl:String,telefono:String){
        val uid =  FirebaseAuth.getInstance().uid ?:""
        val ref =FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        val user =  User(uid,nombre,correo,imagenUrl,telefono)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Nuevo usuario guardado en la base de datos")
                val intent = Intent(this,TripList::class.java)
                intent.putExtra("usuarioActual",user)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                button_register.isClickable = true
                startActivity(intent)
            }.addOnFailureListener {
                Log.d("RegisterActivity","Error al guardar la base de datos: ${it.message}")
                Toast.makeText(baseContext, "Error: Registrado satisfactoriamente sin sus datos personales",
                    Toast.LENGTH_SHORT).show()
                button_register.isClickable = true
            }
    }
}


