package com.example.unalpool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_trip_passenger.*

class NewTripPassenger : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_passenger)
        supportActionBar?.title = "Buscar Viaje"
            button_searchTrip.setOnClickListener {
                buscarViaje()
            }
    }

    private fun buscarViaje(){
        val campusLlegada = campus_llegada_editText_newTrip_passenger.text.toString().toLowerCase()
        val campusSalida = campus_salida_editText_newTrip_passenger.text.toString().toLowerCase()
        val horaSalida = hora_salida_editText_newTrip_passenger.text.toString()
        Log.d("NewTripPassenger","campus llegada $campusLlegada and campus salida $campusSalida")
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
        if(campusSalida.equals(campusLlegada)) {
            Toast.makeText(baseContext, "los campos no pueden ser iguales",
                Toast.LENGTH_SHORT).show()
            return
        }
    }
}
