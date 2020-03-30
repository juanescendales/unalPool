package com.example.unalpool.Models

import java.sql.Date

class Trip (val id:String, val campusSalida:String, val campusLlegada:String,val fechaDeSalida:String, val horaDeSalida:String, val numeroCupos:Int,val idConductor:String, val nombreConductor:String,val urlFotoConductor:String , val estado:Int = 0) {
    constructor() : this("null", "null", "null", "null","null",0,"null","null","null")
}