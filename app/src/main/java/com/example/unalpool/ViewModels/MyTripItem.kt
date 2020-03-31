package com.example.unalpool.ViewModels

import android.content.Context
import android.content.Intent
import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_row_mytrip.view.*


class MyTripItem(val trip:Trip,val intentPeticiones: Intent,val intentVerMas: Intent,val baseContext: Context, val aplicationContext: Context) : Item<GroupieViewHolder>(){
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
        viewHolder.itemView.peticiones_button_mytrip.setOnClickListener {
            intentPeticiones.putExtra("trip",trip)
            intentPeticiones.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            aplicationContext.startActivity(intentPeticiones)
        }
        viewHolder.itemView.ver_button_mytrip.setOnClickListener {
            intentVerMas.putExtra("trip",trip)
            intentVerMas.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            aplicationContext.startActivity(intentVerMas)
        }
    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mytrip
    }
}