package com.example.unalpool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.unalpool.Models.User
import com.example.unalpool.ViewModels.MyPetitionSuccessItem
import com.example.unalpool.ViewModels.MyTripItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_trip_list.*

class TripList : AppCompatActivity() {

    var usuarioActual:User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)
        verificarSesion()
        usuarioActual = User.usuarioActual
        Log.d("TripList", "usuarioActual.sesionConductor : ${usuarioActual.sesionConductor}")
    }

    override fun onResume() {
        super.onResume()
        usuarioActual = User.usuarioActual
        Log.d("TripList", "usuarioActual.sesionConductor ONRESUME : ${usuarioActual.sesionConductor}")
        fetchTrips()
    }
    private fun fetchTrips() {
        val intentPeticiones = Intent(this,MyPetitionsDriver::class.java)
        val intentVerMas = Intent(this,TripActivity::class.java)
        if (usuarioActual.sesionConductor) {
            val ref = FirebaseDatabase.getInstance().getReference("/viajes")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("TripList", "Entra al on data change")
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    p0.children.forEach {
                        val trip = it.getValue(Trip::class.java)
                        Log.d("TripList", "iterando los viajes: ${trip.toString()}")
                        if (trip != null) {
                            if (trip.idConductor == usuarioActual.uid && (trip.estado == 0 || trip.estado == 1)) {
                                adapter.add(MyTripItem(trip,intentPeticiones,intentVerMas,baseContext,applicationContext))
                            }

                        }

                    }
                    recyclerview_mytrips.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("TripList", "Error al consultar viajes: ${p0.message}")
                }
            })

        } else {
            val ref = FirebaseDatabase.getInstance().getReference("/peticiones")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("TripList", "Entra al on data change")
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    p0.children.forEach {
                        val petition = it.getValue(Petition::class.java)
                        Log.d("TripList", "iterando las peticiones: ${petition.toString()}")
                        if (petition != null) {
                            if (petition.idPasajero == usuarioActual.uid && petition.estado == 1) {
                                adapter.add(MyPetitionSuccessItem(petition))
                            }

                        }

                    }
                    recyclerview_mytrips.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("TripList", "Error al consultar viajes: ${p0.message}")
                }
            })

        }
    }
    private fun verificarSesion(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(usuarioActual.sesionConductor){
            menuInflater.inflate(R.menu.nav_menu_conductor,menu)
        }else{
            menuInflater.inflate(R.menu.nav_menu,menu)
        }

        return super.onCreateOptionsMenu(menu)

        }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_mis_solicitudes ->{
                val intent = Intent(this, MyPetitions::class.java)
                startActivity(intent)
            }
            R.id.menu_nuevo_viaje ->{
                if(usuarioActual.esConductor){
                    val intent = Intent(this, NewTripDriver::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, NewTripPassenger::class.java)
                    startActivity(intent)
                }
            }
            R.id.menu_mi_perfil ->{
                val intent = Intent(this, MyProfile::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
