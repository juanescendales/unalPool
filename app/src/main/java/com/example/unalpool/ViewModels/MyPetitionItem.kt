package com.example.unalpool.ViewModels

import com.example.unalpool.Models.Petition
import com.example.unalpool.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_mypetitions.view.*

class MyPetitionItem(val petition: Petition) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.fecha_textView_mypetition.text = petition.fechaSalida
        viewHolder.itemView.hora_textView_mypetition.text = petition.horaSalida
        viewHolder.itemView.campusSalida_textView_mypetition.text = petition.campusSalida
        viewHolder.itemView.campusLlegada_textView_mypetition.text = petition.campusLlegada
    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mypetitions
    }
}
