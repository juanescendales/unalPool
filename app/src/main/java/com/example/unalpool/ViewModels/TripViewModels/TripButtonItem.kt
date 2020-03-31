package com.example.unalpool.ViewModels.TripViewModels

import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_activity_successbutton.view.*

class TripButtonItem(val trip: Trip) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.success_tripactivity_button.setOnClickListener {
            trip.estado = 2
            val ref = FirebaseDatabase.getInstance().getReference("/viajes").child("/${trip.id}")
            ref.updateChildren(trip.toMap())
        }

    }
    override fun getLayout(): Int {
        return R.layout.trip_row_activity_successbutton
    }
}