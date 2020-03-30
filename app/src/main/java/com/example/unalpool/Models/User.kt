package com.example.unalpool.Models

import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class User( val uid:String, val nombre:String, val correo:String, val imagenUrl:String , val telefono: String , val esConductor:Boolean = false, val sesionConductor:Boolean = false, val licenciaConduccionURL:String = "") {
    constructor():this("null","null","null","null","null")

}
