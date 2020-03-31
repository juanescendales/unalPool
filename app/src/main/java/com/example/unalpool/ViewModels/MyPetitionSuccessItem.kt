package com.example.unalpool.ViewModels

import com.example.unalpool.Models.Petition
import com.example.unalpool.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_mypetitions_success.view.*

class MyPetitionSuccessItem(val petition: Petition) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.fecha_textView_mypetitionsuccess.text = petition.fechaSalida
        viewHolder.itemView.hora_textView_mypetitionsuccess.text = petition.horaSalida
        viewHolder.itemView.campusLlegada_textView_mypetitionsuccess.text = petition.campusLlegada
        viewHolder.itemView.campusSalida_textView_mypetitionsuccess.text = petition.campusSalida

    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mypetitions_success
    }
}
