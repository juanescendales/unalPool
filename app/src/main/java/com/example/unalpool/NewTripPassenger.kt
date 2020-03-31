package com.example.unalpool

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unalpool.Models.Petition
import com.example.unalpool.Models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_new_trip_passenger.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewTripPassenger : AppCompatActivity() {
    private var hora:String = ""
    private var fecha:String = ""
    private val formatoFecha = SimpleDateFormat("dd MMM, YYYY")
    private val formatoHora = SimpleDateFormat("hh:mm a")
    var usuarioActual: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_passenger)
        supportActionBar?.title = "Buscar Viaje"
        usuarioActual = User.usuarioActual

        button_date_searchtrip.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                fecha = formatoFecha.format(selectedDate.time)
                button_date_searchtrip.text = fecha
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
                button_hour_searchtrip.text = hora
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

        terminarBusquedaViaje(intent)

    }
    private fun terminarBusquedaViaje(intent:Intent){

        val ref = FirebaseDatabase.getInstance().getReference("/peticiones")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("NewTripPassenger", "Entra al on data change")
                val arrayIndicesPeticiones: ArrayList<String> = ArrayList()
                p0.children.forEach() {
                    val petition = it.getValue(Petition::class.java)
                    if(petition != null && petition.estado == 0){
                        val index = petition.idViaje
                        Log.d("NewTripPassenger", "Indice de viaje $index")
                        arrayIndicesPeticiones.add(index)
                    }
                }
                intent.putExtra("arrayIndicesPeticiones",arrayIndicesPeticiones)
                startActivity(intent)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("NewTripPassenger", "Error al consultar viajes: ${p0.message}")
            }
        })
    }
}
