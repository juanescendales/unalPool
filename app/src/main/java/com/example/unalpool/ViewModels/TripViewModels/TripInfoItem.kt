package com.example.unalpool.ViewModels.TripViewModels

import android.content.Intent
import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_activity_info.view.*

class TripInfoItem(val trip: Trip) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.fecha_tripactivity_textView.text = trip.fechaDeSalida
        viewHolder.itemView.hora_tripactivity_textView.text = trip.horaDeSalida
        viewHolder.itemView.origen_tripactivity_textView.text = trip.campusSalida
        viewHolder.itemView.destino_tripactivity_textView.text = trip.campusLlegada
        viewHolder.itemView.cupos_tripactivity_textView.text = trip.numeroCupos.toString()
        val url = trip.urlFotoConductor
        if(url !=""){
            Picasso.get().load(url).into(viewHolder.itemView.photo_driver_tripactivity_imageView)
        }

    }
    override fun getLayout(): Int {
        return R.layout.trip_row_activity_info
    }
}
