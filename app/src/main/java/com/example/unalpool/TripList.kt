package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TripList : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list_passenger)
        val user = FirebaseAuth.getInstance().currentUser
        val infoUser = database.child("usuarios").child(user?.uid.toString())
    }
}
