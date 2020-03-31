package com.example.unalpool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unalpool.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity  : AppCompatActivity(){
    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        cuenta_no_existe_textView_log_in.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            login()
        }

    }
    private fun login(){
        login_button.isClickable = false
        val email = email_editText_login.text.toString()
        val password = password_editText_login.text.toString()
        if(email == "" || password == "") {
            Toast.makeText(baseContext, "Llena todos los campos",
                Toast.LENGTH_SHORT).show()
            return
        }
        val task = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this,TripList::class.java)
                    val uid = FirebaseAuth.getInstance().uid
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val usuarioActual:User = dataSnapshot.getValue(User::class.java)!!
                            Log.d("TripList", "Objeto: ${usuarioActual.nombre}")
                            Log.d("Sing in", "signInWithEmail:success")
                            Toast.makeText(baseContext, "Ingreso satisfactorio",
                                Toast.LENGTH_SHORT).show()
                            User.usuarioActual = usuarioActual
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            login_button.isClickable = false
                            startActivity(intent)

                        }
                        override fun onCancelled(dataSnapshot: DatabaseError) {
                            Log.d("TripList", "loadPost:onCancelled")
                            FirebaseAuth.getInstance().signOut()
                            login_button.isClickable = true
                        }
                    }
                    database.child("usuarios").child(uid.toString()).addListenerForSingleValueEvent(postListener)


                } else {

                    Log.w("Sing in", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Fallo al iniciar sesion: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                    login_button.isClickable = true

                }

            }
    }
}