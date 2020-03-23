package com.example.unalpool

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Log.d("Register","Hola1")
        button_register.setOnClickListener {
            crearRegistro()
        }
        cuenta_existe_textView_register.setOnClickListener {
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun crearRegistro(){
        val email = email_editText_register.text.toString()
        val password = password_editText_register.text.toString()
        val phone = phone_editText_register.text.toString()
        Log.d("Register","email: $email and password $password")

        if(password.length < 6){
            Toast.makeText(baseContext, "Contraseña minimo de 6 caracteres",
                Toast.LENGTH_SHORT).show()
            return
        }

        if(password.length > 16){
            Toast.makeText(baseContext, "Contraseña maximo de 20 caracteres",
                Toast.LENGTH_SHORT).show()
            return
        }

        if(!("@unal.edu.co" in email)){
            Toast.makeText(baseContext, "Solo son validos correos de la universidad",
                Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "Entra a firebase")
                    Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")
                    Log.d("Register", "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Registrado satisfactoriamente",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "createUserWithEmail:failure", it.exception)
                    Toast.makeText(baseContext, "Fallo en el proceso de registro: ${it.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }
    }
}

