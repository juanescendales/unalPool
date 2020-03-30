package com.example.unalpool.ViewModels

import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_row_mytrip.view.*


class MyTripItem(val trip:Trip) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name_driver_textView.text = trip.nombreConductor
        viewHolder.itemView.hora_textView_mytrip.text = trip.horaDeSalida
        viewHolder.itemView.origen_textView_mytrip.text = trip.campusSalida
        viewHolder.itemView.destino_textView_mytrip.text = trip.campusLlegada
        Picasso.get().load(trip.urlFotoConductor).into(viewHolder.itemView.photo_driver_imageView_mytrips)
        /*if(trip.estado == 0){
            viewHolder.itemView.estado_imageView_mytrip.setImageResource(@android:drawable/presence_away)

        }*/
    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mytrip
    }
}