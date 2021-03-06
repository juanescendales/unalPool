package com.example.unalpool

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.unalpool.Models.Trip
import com.example.unalpool.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_trip_driver.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class NewTripDriver: AppCompatActivity() {
    private var hora: Date = Calendar.getInstance().time
    private var fecha: Date = Calendar.getInstance().time
    private val formatoFecha = SimpleDateFormat("dd MMM, YYYY")
    private val formatoHora = SimpleDateFormat("hh:mm a")
    var usuarioActual: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_driver)
        supportActionBar?.title = "Nuevo Viaje"
        usuarioActual = User.usuarioActual
        button_date_createtrip.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    Log.d("NewTripDriver","Fecha seleccionada: ${selectedDate.time}")
                    fecha = selectedDate.time
                    button_date_createtrip.text = formatoFecha.format(selectedDate.time)
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button_hour_createtrip.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    hora = selectedTime.time
                    Log.d("NewTripDriver","Hora seleccionada: ${selectedTime.time}")
                    button_hour_createtrip.text = formatoHora.format(selectedTime.time)
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }

        button_createTrip.setOnClickListener {
            crearViaje()
        }
    }

    private fun crearViaje() {
        val campusLlegada =
            campus_llegada_editText_newTrip_driver.text.toString().toLowerCase().trim()
        val campusSalida =
            campus_salida_editText_newTrip_driver.text.toString().toLowerCase().trim()
        val cuposDisponiblesString =
            cupos_disponibles_editNumber_newTrip_driver.text.toString().trim()
        val fechaString:String = formatoFecha.format(fecha)
        val horaString:String = formatoHora.format(hora)
        Log.d(
            "NewTripDriver",
            "campus llegada $campusLlegada and campus salida $campusSalida cupos disponibles: $cuposDisponiblesString"
        )
        if (campusLlegada == "" || campusSalida == "" || cuposDisponiblesString == "" || horaString == "" || fechaString == "") {
            Toast.makeText(
                baseContext, "Llena todos los campos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val cuposDisponibles = cuposDisponiblesString.toInt()
        if (campusLlegada != "minas" && campusLlegada != "volador") {
            Toast.makeText(
                baseContext, "Campus de llegada debe ser minas o volador",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (campusSalida != "minas" && campusSalida != "volador") {
            Toast.makeText(
                baseContext, "Campus de salida debe ser minas o volador",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (campusSalida.equals(campusLlegada)) {
            Toast.makeText(
                baseContext, "los campos no pueden ser iguales",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (cuposDisponibles >= 7) {
            Toast.makeText(
                baseContext, "El numero de cupos es menor a 7",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (cuposDisponibles < 1) {
            Toast.makeText(
                baseContext, "El numero de cupos debe ser mayor a 0",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        //Validacion sentido de fecha
        val hoy = Calendar.getInstance().time.time
        val tiempoSeleccionado = fecha.time + (hora.time - hoy)

        Log.d("NewTripDriver","Valor hoy: $hoy")
        Log.d("NewTripDriver","Valor fecha seleccionada: $fecha")

        if (hoy > tiempoSeleccionado) {
            Toast.makeText(
                baseContext, "Selecciona una fecha valida",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        //Validacion sentido de fecha

        val ref = FirebaseDatabase.getInstance().getReference("/viajes").push()
        val trip = Trip(
            ref.key.toString(),
            campusSalida,
            campusLlegada,
            fechaString,
            horaString,
            cuposDisponibles,
            usuarioActual.uid,
            usuarioActual.nombre,
            usuarioActual.imagenUrl
        )
        ref.setValue(trip)
            .addOnSuccessListener {
                Log.d("NewTripDriver", "Nuevo viaje creado")
                Toast.makeText(
                    baseContext, "Nuevo viaje creado",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }.addOnFailureListener {
                Log.d("NewTripDriver", "Error al guardar la base de datos: ${it.message}")
                Toast.makeText(
                    baseContext,
                    "Error: Registrado satisfactoriamente sin sus datos personales",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

