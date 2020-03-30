package com.example.unalpool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.unalpool.Models.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.unalpool.Models.User
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
        usuarioActual = intent.extras.get("usuarioActual") as User
    }

    override fun onResume() {
        super.onResume()
        fetchTrips()
    }
    private fun fetchTrips(){
        Log.d("TripList", "usuarioActual.esConductor : ${usuarioActual.esConductor}")
        if(usuarioActual.esConductor && usuarioActual.sesionConductor){
            val ref = FirebaseDatabase.getInstance().getReference("/viajes")
            ref.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("TripList", "Entra al on data change")
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    p0.children.forEach{
                        val trip = it.getValue(Trip::class.java)
                        Log.d("TripList", "iterando los viajes: ${trip.toString()}")
                        if(trip != null){
                            if(trip.idConductor == usuarioActual.uid && (trip.estado == 0 || trip.estado == 1)){
                                adapter.add(MyTripItem(trip))
                            }

                        }

                    }
                    recyclerview_mytrips.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("TripList", "Error al consultar viajes: ${p0.message}")
                }
            })

        }else{

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
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_nuevo_viaje ->{
                if(usuarioActual.esConductor){
                    val intent = Intent(this, NewTripDriver::class.java)
                    intent.putExtra("user.id",usuarioActual.uid)
                    intent.putExtra("user.nombre",usuarioActual.nombre)
                    intent.putExtra("user.imagenUrl",usuarioActual.imagenUrl)
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
