package com.example.unalpool.Models
import java.io.Serializable

class User( val uid:String, val nombre:String, val correo:String, val imagenUrl:String , val telefono: String , val esConductor:Boolean = false, val sesionConductor:Boolean = false, val licenciaConduccionURL:String = ""): Serializable {
    constructor():this("null","null","null","null","null")

}
