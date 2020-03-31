package com.example.unalpool

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.unalpool.Models.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class MyProfile : AppCompatActivity() {
    var usuarioActual:User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        supportActionBar?.title = "Mi Perfil"
        usuarioActual = User.usuarioActual
        val url = usuarioActual.imagenUrl
        if(url !=""){
            Picasso.get().load(url).into(photo_driver_myprofile_imageView)
        }
        nombre_myprofile_textView.text =usuarioActual.nombre
        correo_myprofile_textView.text =usuarioActual.correo
        telefono_myprofile_textView.text = usuarioActual.telefono
        if(!usuarioActual.esConductor){
            es_conductor_myprofile_textView.text = "No"
            quiero_ser_conductor_my_profile_button.text = "QUIERO SER CONDUCTOR"

            quiero_ser_conductor_my_profile_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                quiero_ser_conductor_my_profile_button.isClickable = false
                startActivityForResult(intent,0)

            }
        }else if(usuarioActual.esConductor && usuarioActual.sesionConductor){
            mensaje_myprofile_textView.visibility = View.INVISIBLE
            es_conductor_myprofile_textView.text = "Si"
            quiero_ser_conductor_my_profile_button.text = "MODO PASAJERO"

            quiero_ser_conductor_my_profile_button.setOnClickListener {
                usuarioActual.sesionConductor =false
                val ref = FirebaseDatabase.getInstance().getReference("/usuarios").child("/${usuarioActual.uid}")
                ref.updateChildren(usuarioActual.toMap())
                quiero_ser_conductor_my_profile_button.isClickable = false

                User.usuarioActual.sesionConductor = false
                val intent = Intent(this,TripList::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }


        }else if(usuarioActual.esConductor && !usuarioActual.sesionConductor){
            mensaje_myprofile_textView.visibility = View.INVISIBLE
            es_conductor_myprofile_textView.text = "Si"
            quiero_ser_conductor_my_profile_button.text = "MODO CONDUCTOR"

            quiero_ser_conductor_my_profile_button.setOnClickListener{
                usuarioActual.sesionConductor =true
                val ref = FirebaseDatabase.getInstance().getReference("/usuarios").child("/${usuarioActual.uid}")
                ref.updateChildren(usuarioActual.toMap())
                quiero_ser_conductor_my_profile_button.isClickable = false

                User.usuarioActual.sesionConductor = true
                val intent = Intent(this,TripList::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }else{
            Toast.makeText(baseContext, "Error al guardar la imagen",
                Toast.LENGTH_SHORT).show()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            val selectedPhotoUri = data.data
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity","Guardado de foto satisfactorio: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {task ->
                        val downLoadReference = task.toString()
                        Log.d("RegisterActivity","Guardado de foto satisfactorio: $downLoadReference")
                        //Actualizar estados
                        usuarioActual.esConductor = true
                        usuarioActual.sesionConductor =true
                        usuarioActual.licenciaConduccionURL = downLoadReference
                        val ref = FirebaseDatabase.getInstance().getReference("/usuarios").child("/${usuarioActual.uid}")
                        ref.updateChildren(usuarioActual.toMap())

                        User.usuarioActual.esConductor = true
                        User.usuarioActual.sesionConductor =true
                        User.usuarioActual.licenciaConduccionURL = downLoadReference
                        val intent = Intent(this,TripList::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity","Error al guardar foto: ${it.message}")
                    Toast.makeText(baseContext, "Error al guardar la imagen",
                        Toast.LENGTH_SHORT).show()
                    quiero_ser_conductor_my_profile_button.isClickable = true
                }
        }
    }


}
