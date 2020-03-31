package com.example.unalpool.Models
import java.io.Serializable

class User(val uid:String, val nombre:String, val correo:String, val imagenUrl:String, val telefono: String, var esConductor:Boolean = false, var sesionConductor:Boolean = false, var licenciaConduccionURL:String = ""): Serializable {
    constructor():this("null","null","null","null","null")
    companion object{
        var usuarioActual = User()
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "nombre" to nombre,
            "correo" to correo,
            "imagenUrl" to imagenUrl,
            "telefono" to telefono,
            "esConductor" to esConductor,
            "sesionConductor" to sesionConductor,
            "licenciaConduccionURL" to licenciaConduccionURL
        )
    }
}
