package com.example.unalpool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unalpool.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*


class MainActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        main_button_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        main_button_login.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val uid = FirebaseAuth.getInstance().uid
        val intent = Intent(this,TripList::class.java)
        if(uid!= null){
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usuarioActual: User = dataSnapshot.getValue(User::class.java)!!
                    Log.d("TripList", "Objeto: ${usuarioActual.nombre}")
                    Log.d("Sing in", "signInWithEmail:success")
                    Toast.makeText(baseContext, "Ingreso satisfactorio",
                        Toast.LENGTH_SHORT).show()
                    intent.putExtra("usuarioActual",usuarioActual)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    Log.d("TripList", "loadPost:onCancelled")
                    FirebaseAuth.getInstance().signOut()
                }
            }
            database.child("usuarios").child(uid.toString()).addListenerForSingleValueEvent(postListener)
        }
    }

}
