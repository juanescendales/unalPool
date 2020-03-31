package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.User
import com.example.unalpool.ViewModels.MyPetitionItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_my_petitions.*

class MyPetitions : AppCompatActivity() {
    var usuarioActual: User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_petitions)
        supportActionBar?.title = "Mis solicitudes"
        usuarioActual = User.usuarioActual
        fetchPetitions()
    }

    private fun fetchPetitions(){
        val ref = FirebaseDatabase.getInstance().getReference("/peticiones")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("NewTripPassenger", "Entra al on data change")
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach() {
                    val petition = it.getValue(Petition::class.java)
                    if(petition != null && petition.estado == 0 && petition.idPasajero == usuarioActual.uid){
                        adapter.add(MyPetitionItem(petition))
                    }
                }
                recyclerview_mypetitions.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("NewTripPassenger", "Error al consultar viajes: ${p0.message}")
            }
        })
    }
}
