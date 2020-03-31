package com.example.unalpool.Models

class Petition (val id:String, var estado:Int, val idViaje:String, val idPasajero:String , val fechaSalida:String,  val horaSalida:String,val campusSalida:String, val campusLlegada:String, val nombrePasajero:String, val urlImagenPasajero:String) {
    constructor() : this("null", 0,"null","null","null","null","null","null","null","null")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "estado" to estado,
            "idViaje" to idViaje,
            "idPasajero" to idPasajero,
            "fechaSalida" to fechaSalida,
            "horaSalida" to horaSalida,
            "campusSalida" to campusSalida,
            "campusLlegada" to campusLlegada,
            "nombrePasajero" to nombrePasajero,
            "urlImagenPasajero" to urlImagenPasajero
        )
    }

}