package com.example.unalpool.Models

class User( val uid:String, val nombre:String, val correo:String, val imagenUrl:String , val telefono: String , val esConductor:Boolean = false, val sesionConductor:Boolean = false, val licenciaConduccionURL:String = "") {
    constructor():this("null","null","null","null","null")

}
