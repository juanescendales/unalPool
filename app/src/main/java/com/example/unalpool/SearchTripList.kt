package com.example.unalpool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.unalpool.Models.Trip
import com.example.unalpool.Models.User
import com.example.unalpool.ViewModels.TripSearchItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_search_trip_list.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchTripList : AppCompatActivity() {
    var usuarioActual: User = User()
    private val formatoHora = SimpleDateFormat("hh:mm a")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_trip_list)
        usuarioActual = User.usuarioActual
        fetchTrips()
    }

    private fun fetchTrips(){
        val campusLlegada:String = intent.extras.get("campusLlegada").toString()
        val campusSalida:String = intent.extras.get("campusSalida").toString()
        val tolerancia:Int = intent.extras.get("tolerancia").toString().toInt()
        val hora:String = intent.extras.get("hora").toString()
        val arrayIndicesPeticiones:ArrayList<String> = intent.extras.get("arrayIndicesPeticiones") as ArrayList<String>
        val fecha:String = intent.extras.get("fecha").toString()


        val intent = Intent(this,NewTripPassenger::class.java)
        intent.putExtra("usuarioActual",usuarioActual)
        val ref = FirebaseDatabase.getInstance().getReference("/viajes")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                Log.d("SearchTripList", "Entra al on data change")
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach{
                    val trip = it.getValue(Trip::class.java)

                    if(trip != null){
                        if( trip.estado == 0 && (trip.campusLlegada == campusLlegada && trip.campusSalida == campusSalida && trip.fechaDeSalida == fecha) ){
                            Log.d("SearchTripList", "iterando los viajes aceptados: ${trip.id}")
                            val horaViaje  =formatoHora.parse(trip.horaDeSalida).time
                            val horaBusqueda = formatoHora.parse(hora).time
                            val minutosDiferencia = (Math.abs(horaViaje-horaBusqueda) / 60000)
                            Log.d("SearchTripList", "diferencia los viajes aceptados: $minutosDiferencia")
                            if(trip.id !in arrayIndicesPeticiones && tolerancia >= minutosDiferencia){
                                adapter.add(TripSearchItem(trip,usuarioActual, baseContext,applicationContext,intent))
                            }
                        }
                    }
                }

                recyclerview_searchTrips.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("TripList", "Error al consultar viajes: ${p0.message}")
            }
        })

    }


}
