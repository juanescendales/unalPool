package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewTripPassenger : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_passenger)
        supportActionBar?.title = "Nuevo Viaje"
    }
}
