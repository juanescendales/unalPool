package com.example.unalpool

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_trip_driver.*
import kotlinx.android.synthetic.main.activity_new_trip_passenger.*
import java.text.SimpleDateFormat
import java.util.*

class NewTripPassenger : AppCompatActivity() {
    private var hora:String = ""
    private var fecha:String = ""
    private val formatoFecha = SimpleDateFormat("dd MMM, YYYY")
    private val formatoHora = SimpleDateFormat("hh:mm a")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_passenger)
        supportActionBar?.title = "Buscar Viaje"
        button_date_searchtrip.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                fecha = formatoFecha.format(selectedDate.time)
                button_date_createtrip.text = fecha
            },
                now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        button_hour_searchtrip.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                hora =  formatoHora.format(selectedTime.time)
                button_hour_createtrip.text = hora
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
            timePicker.show()
        }
        button_searchTrip.setOnClickListener {
            buscarViaje()
        }
    }

    private fun buscarViaje(){
        val campusLlegada = campus_llegada_editText_newTrip_passenger.text.toString().toLowerCase()
        val campusSalida = campus_salida_editText_newTrip_passenger.text.toString().toLowerCase()
        val toleranciaString = tolerancia_editText_newTrip_passenger.text.toString()
        Log.d("NewTripPassenger","campus llegada $campusLlegada and campus salida $campusSalida")
        if(campusLlegada == "" || campusSalida == "" || toleranciaString == "" || hora == "" || fecha == "") {
            Toast.makeText(baseContext, "Llena todos los campos",
                Toast.LENGTH_SHORT).show()
            return
        }
        val tolerancia = toleranciaString.toInt()
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
        if(tolerancia < 0){
            Toast.makeText(baseContext, "Tolerancia minima 0 min",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(tolerancia > 60){
            Toast.makeText(baseContext, "Tolerancia maxima 60 min",
                Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, SearchTripList::class.java)
        intent.putExtra("campusLlegada",campusLlegada)
        intent.putExtra("campusSalida",campusSalida)
        intent.putExtra("tolerancia",tolerancia)
        intent.putExtra("hora",hora)
        intent.putExtra("fecha",fecha)
        startActivity(intent)
    }
}
