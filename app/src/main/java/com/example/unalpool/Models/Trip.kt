package com.example.unalpool.Models

import java.sql.Date

class Trip (val id:String, val campusSalida:String, val campusLlegada:String, val horaDeSalida:String, val numeroCupos:Int, val estado:Int = 0) {
    constructor() : this("null", "null", "null", "null",0)
}