package com.example.unalpool.ViewModels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.widget.Toast
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.example.unalpool.Models.User
import com.example.unalpool.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_search.view.*


class TripSearchItem(val trip:Trip, val pasajero: User, val baseContext: Context,val aplicationContext: Context,val intent: Intent) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name_driver_textView.text = trip.nombreConductor
        viewHolder.itemView.hora_textView.text = trip.horaDeSalida
        viewHolder.itemView.fecha_textView.text = trip.fechaDeSalida
        viewHolder.itemView.origen_textView.text = trip.campusSalida
        viewHolder.itemView.destino_textView.text = trip.campusLlegada
        val url = trip.urlFotoConductor
        if(url !=""){
            Picasso.get().load(url).into(viewHolder.itemView.photo_driver_imageView)
        }

        viewHolder.itemView.viajar_button_searchTrip.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/peticiones").push()
            val idPasajero = FirebaseAuth.getInstance().uid
            if(idPasajero!= null){
                val petition =  Petition(ref.key.toString(),0,trip.id,idPasajero,trip.fechaDeSalida,trip.horaDeSalida,trip.campusSalida,trip.campusLlegada,pasajero.nombre,pasajero.imagenUrl)
                ref.setValue(petition).addOnSuccessListener {
                    Log.d("SearchTripList","Peticion creada satisfactoriamente")
                    Toast.makeText(baseContext, "Peticion creada satisfactoriamente",
                        Toast.LENGTH_SHORT).show()
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    aplicationContext.startActivity(intent)

                }.addOnFailureListener {
                    Log.d("SearchTripList","Error al guardar la base de datos: ${it.message}")
                    Toast.makeText(baseContext, "Error: No se registro la peticion",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }


    }
    override fun getLayout(): Int {
        return R.layout.trip_row_search
    }
}

