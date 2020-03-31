package com.example.unalpool.ViewModels.TripViewModels

import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_activity_passengers.view.*

class TripPassengerItem(val petition: Petition) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.nombre_passenger_tripactivity_textView.text = petition.nombrePasajero
        val url = petition.urlImagenPasajero
        if(url !=""){
            Picasso.get().load(url).into(viewHolder.itemView.photo_passenger_tripactivity_imageView)
        }
    }
    override fun getLayout(): Int {
        return R.layout.trip_row_activity_passengers
    }
}
