package com.example.apliacaciondomotica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CrearRutinaActivity : AppCompatActivity() {

    private lateinit var spinnerDispositivos: Spinner
    private lateinit var timePicker: TimePicker
    private lateinit var btnGuardarRutina: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        spinnerDispositivos = findViewById(R.id.spinner_dispositivos)
        timePicker = findViewById(R.id.time_picker)
        btnGuardarRutina = findViewById(R.id.btn_guardar_rutina)

        btnGuardarRutina.setOnClickListener {
            val dispositivoSeleccionado = spinnerDispositivos.selectedItem.toString()
            val hora = timePicker.hour
            val minutos = timePicker.minute

            // Guardar la rutina en SharedPreferences
            val sharedPreferences = getSharedPreferences("Rutinas", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("rutina_$hora:$minutos", dispositivoSeleccionado)
            editor.apply()

            Toast.makeText(this, "Rutina guardada para las $hora:$minutos", Toast.LENGTH_SHORT).show()

            // Regresar a la actividad de rutinas
            val intent = Intent(this, RutinasActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual
        }
    }
}
