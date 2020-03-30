package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewTripDriver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_driver)

        supportActionBar?.title = "Nuevo Viaje"
    }
}
