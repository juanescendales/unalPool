package com.example.unalpool.ViewModels

import com.example.unalpool.Models.Petition
import com.example.unalpool.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_mypetition_driver.view.*


class MyPetitionDriverItem(val petition: Petition) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.name_passenger_textView.text = petition.nombrePasajero
        val url = petition.urlImagenPasajero
        if(url !=""){
            Picasso.get().load(url).into(viewHolder.itemView.photo_passenger_imageView)
        }
        viewHolder.itemView.button_aceptar_driver.setOnClickListener {
            petition.estado = 1
            val ref = FirebaseDatabase.getInstance().getReference("/peticiones").child("/${petition.id}")
            ref.updateChildren(petition.toMap())

        }
        viewHolder.itemView.button_rechazar_driver.setOnClickListener {
            petition.estado = 2
            val ref = FirebaseDatabase.getInstance().getReference("/peticiones").child("/${petition.id}")
            ref.updateChildren(petition.toMap())
        }
    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mypetition_driver
    }
}
