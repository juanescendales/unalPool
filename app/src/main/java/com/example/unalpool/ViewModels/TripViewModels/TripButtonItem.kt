package com.example.unalpool.ViewModels.TripViewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.example.unalpool.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.trip_row_activity_successbutton.view.*

class TripButtonItem(val trip: Trip,val intent: Intent,val aplicationContext:Context) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.success_tripactivity_button.setOnClickListener {
            trip.estado = 2
            val ref = FirebaseDatabase.getInstance().getReference("/viajes").child("/${trip.id}")
            ref.updateChildren(trip.toMap()).addOnSuccessListener {
                buscarPeticionesRelacionadas()
            }

        }

    }
    override fun getLayout(): Int {
        return R.layout.trip_row_activity_successbutton
    }
    private fun deshabilitarPeticion(petition:Petition) {
        petition.estado = 2
        val ref = FirebaseDatabase.getInstance().getReference("/peticiones").child("/${petition.id}")
        ref.updateChildren(petition.toMap())
    }
    private fun buscarPeticionesRelacionadas(){
        val ref =FirebaseDatabase.getInstance().getReference("/peticiones")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach() {
                    val petition = it.getValue(Petition::class.java)
                    if(petition != null && petition.estado == 1 && petition.idViaje == trip.id){
                        deshabilitarPeticion(petition)
                    }
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                aplicationContext.startActivity(intent)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("NewTripPassenger", "Error al consultar viajes: ${p0.message}")
            }
        })
    }
}
