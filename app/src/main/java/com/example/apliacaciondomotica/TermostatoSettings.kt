package com.example.apliacaciondomotica

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TermostatoSettings: AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var volumeText: TextView
    private lateinit var buttonBorrar: Button
    private lateinit var buttonActualizar: Button
    private lateinit var progressBarActualizar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termostato)

        // Inicializar vistas
        seekBar = findViewById(R.id.volume_seekbar)
        volumeText = findViewById(R.id.volume_text)
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonActualizar = findViewById(R.id.buttonActualizar)
        progressBarActualizar = findViewById(R.id.progressBarActualizar)

        // Obtener preferencias guardadas
        val sharedPreferences = getSharedPreferences("ThermostatSettings", Context.MODE_PRIVATE)
        val savedTemperature = sharedPreferences.getInt("thermostatTemperature", 50)

        // Configurar SeekBar y texto inicial
        seekBar.progress = savedTemperature
        volumeText.text = "Temperatura: ${savedTemperature}ºC"

        val buttonBorrar: Button
        buttonBorrar = findViewById(R.id.buttonBorrar)
        buttonBorrar.setOnClickListener{
            Toast.makeText(this, "Dispositivo Borrado", Toast.LENGTH_SHORT).show()
        }

        // Listener del SeekBar para actualizar el texto
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeText.text = "Temperatura: ${progress}ºC"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Guardar la temperatura actual en SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putInt("thermostatTemperature", seekBar?.progress ?: 0)
                editor.apply()
                Toast.makeText(this@TermostatoSettings, "Temperatura guardada", Toast.LENGTH_SHORT).show()
            }
        })

        // Listener del botón "Borrar dispositivo"
        buttonBorrar.setOnClickListener {
            Toast.makeText(this, "Dispositivo borrado", Toast.LENGTH_SHORT).show()
            // Aquí podrías añadir lógica para borrar el dispositivo de la base de datos
        }

        // Listener del botón "Actualizar Firmware"
        buttonActualizar.setOnClickListener {
            progressBarActualizar.visibility = View.VISIBLE
            Toast.makeText(this, "Firmware actualizado correctamente", Toast.LENGTH_SHORT).show()

            // Simular una actualización con un retraso
            buttonActualizar.postDelayed({
                progressBarActualizar.visibility = View.INVISIBLE
                Toast.makeText(this, "Firmware actualizado correctamente", Toast.LENGTH_SHORT).show()
            }, 3000) // 3 segundos de simulación
        }
    }
}
