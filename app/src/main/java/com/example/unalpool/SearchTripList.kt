package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unalpool.Models.Trip
import com.example.unalpool.ViewModels.MyTripItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_trip_list.*

class SearchTripList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_trip_list)
    }

    private fun fetchTrips(){
        val campusLlegada = intent.extras.get("campusLlegada")
        val campusSalida = intent.extras.get("campusSalida")
        val tolerancia = intent.extras.get("tolerancia")
        val hora = intent.extras.get("hora")
        val fecha = intent.extras.get("fecha")
        val ref = FirebaseDatabase.getInstance().getReference("/viajes")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("TripList", "Entra al on data change")
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    val trip = it.getValue(Trip::class.java)
                    Log.d("TripList", "iterando los viajes: ${trip.toString()}")
                    if(trip != null){
                        if( trip.estado == 0 && (trip.campusLlegada == campusLlegada && trip.campusSalida == campusSalida && trip.fechaDeSalida == fecha) ){
                            adapter.add(MyTripItem(trip))
                        }

                    }

                }
                recyclerview_mytrips.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("TripList", "Error al consultar viajes: ${p0.message}")
            }
        })

    }


}
