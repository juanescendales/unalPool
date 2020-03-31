package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.example.unalpool.ViewModels.MyPetitionDriverItem

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_my_petitions_driver.*


class MyPetitionsDriver : AppCompatActivity() {
    var trip:Trip= Trip()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_petitions_driver)
        supportActionBar?.title = "Peticiones de mi viaje"
        trip = intent.extras.get("trip") as Trip
        fetchPetitions()
    }
    private fun fetchPetitions(){
        val ref = FirebaseDatabase.getInstance().getReference("/peticiones")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("NewTripPassenger", "Entra al on data change")
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach() {
                    val petition = it.getValue(Petition::class.java)
                    if(petition != null && petition.estado == 0 && petition.idViaje == trip.id){
                        adapter.add(MyPetitionDriverItem(petition))
                    }
                }
                recyclerview_petitionsdriver.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("NewTripPassenger", "Error al consultar viajes: ${p0.message}")
            }
        })
    }
}
