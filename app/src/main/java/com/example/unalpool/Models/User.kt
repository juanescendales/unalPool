package com.example.unalpool.Models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class User( val uid:String, val nombre:String, val correo:String, val imagenUrl:String , val telefono: String , val esVendedor:Boolean = false, val sesionVendedor:Boolean = false, val licenciaConduccionURL:String = "") {
    constructor():this("null","null","null","null","null")

    companion object {
        @JvmStatic
        fun traerBaseDeDatos(uid: String, mDataBase: DatabaseReference): User {
            var user: User
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        user = dataSnapshot.getValue(User::class.java)!!
                    }
                }
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    user = User()
                }
            }
            mDataBase.addValueEventListener(postListener)
            return user
        }
    }
}
