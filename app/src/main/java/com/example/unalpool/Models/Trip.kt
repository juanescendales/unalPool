package com.example.unalpool.Models

import java.io.Serializable


class Trip (val id:String, val campusSalida:String, val campusLlegada:String, val fechaDeSalida:String, val horaDeSalida:String, val numeroCupos:Int, val idConductor:String, val nombreConductor:String, val urlFotoConductor:String, var estado:Int = 0):Serializable {
    constructor() : this("null", "null", "null", "null","null",0,"null","null","null")
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "campusSalida" to campusSalida,
            "campusLlegada" to campusLlegada,
            "fechaDeSalida" to fechaDeSalida,
            "horaDeSalida" to horaDeSalida,
            "numeroCupos" to numeroCupos,
            "idConductor" to idConductor,
            "nombreConductor" to nombreConductor,
            "urlFotoConductor" to urlFotoConductor,
            "estado" to estado
        )
    }
}