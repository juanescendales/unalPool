package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_trip_driver.*

class NewTripDriver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_driver)
        supportActionBar?.title = "Nuevo Viaje"

        button_createTrip.setOnClickListener {
            crearViaje()
        }
    }

    private fun crearViaje(){
        val campusLlegada = campus_llegada_editText_newTrip_driver.text.toString().toLowerCase()
        val campusSalida = campus_salida_editText_newTrip_driver.text.toString().toLowerCase()
        val horaSalida = hora_salida_editText_newTrip_driver.text.toString()
        val cuposDisponibles = cupos_disponibles_editNumber_newTrip_driver.text.toString().toInt()
        Log.d("NewTripDriver","campus llegada $campusLlegada and campus salida $campusSalida cupos disponibles: $cuposDisponibles")
        if(campusLlegada != "minas" && campusLlegada != "volador"){
            Toast.makeText(baseContext, "Campus de llegada debe ser minas o volador",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(campusSalida != "minas" && campusSalida != "volador"){
            Toast.makeText(baseContext, "Campus de salida debe ser minas o volador",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(campusSalida.equals(campusLlegada)){
            Toast.makeText(baseContext, "los campos no pueden ser iguales",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(cuposDisponibles >= 7){
            Toast.makeText(baseContext, "El numero de cupos es menor a 7",
                Toast.LENGTH_SHORT).show()
            return
        }

    }
}
