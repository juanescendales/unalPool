package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.example.unalpool.Models.User
import com.example.unalpool.ViewModels.MyPetitionSuccessItem
import com.example.unalpool.ViewModels.TripViewModels.TripButtonItem
import com.example.unalpool.ViewModels.TripViewModels.TripInfoItem
import com.example.unalpool.ViewModels.TripViewModels.TripPassengerItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.activity_trip_list.*

class TripActivity : AppCompatActivity() {
    var usuarioActual: User = User()
    var tripActual:Trip = Trip()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        usuarioActual = User.usuarioActual
        tripActual = intent.extras.get("trip") as Trip
        val adapter = GroupAdapter<GroupieViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("/peticiones")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("TripList", "Entra al on data change")
                val adapter = GroupAdapter<GroupieViewHolder>()
                adapter.add(TripInfoItem(tripActual))
                p0.children.forEach {
                    val petition = it.getValue(Petition::class.java)
                    if (petition != null) {
                        if (petition.idViaje == tripActual.id && (tripActual.estado == 1 || tripActual.estado == 0 )){
                            adapter.add(TripPassengerItem(petition))
                        }
                    }
                }
                adapter.add(TripButtonItem(tripActual))
                recyclerview_trip_activity.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("TripList", "Error al consultar viajes: ${p0.message}")
            }
        })
        recyclerview_trip_activity.adapter = adapter
    }
}
