package com.example.unalpool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    val database = FirebaseDatabase.getInstance().getReference()
    var usuarioActual:User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)
        verificarSesion()
        val uid = FirebaseAuth.getInstance().uid
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usuarioActual = dataSnapshot.getValue(User::class.java)!!
                Log.d("TripList", "Objeto: ${usuarioActual.nombre}")
            }
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d("TripList", "loadPost:onCancelled")
                FirebaseAuth.getInstance().signOut()
                verificarSesion()
            }
        }
        database.child("usuarios").child(uid.toString()).addValueEventListener(postListener)

        //Vistas de lista resumidas
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(MyTripItem())
        adapter.add(MyTripItem())
        adapter.add(MyTripItem())
        recyclerview_mytrips.adapter = adapter


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
