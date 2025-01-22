package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnOff: Button
    private lateinit var tvStatus: TextView
    private lateinit var volumeStatus: TextView
    private lateinit var blindsPercentage: TextView
    private lateinit var lightStatus: TextView
    private lateinit var termostatoStatus: TextView
    private lateinit var termostatoButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOff = findViewById(R.id.btn_off)
        tvStatus = findViewById(R.id.tv_status)
        volumeStatus = findViewById(R.id.volume_status)
        blindsPercentage = findViewById(R.id.blinds_percentage)
        lightStatus = findViewById(R.id.light_status)
        termostatoStatus = findViewById(R.id.termostato_Status)
        termostatoButton = findViewById(R.id.btn_Termostato)

        // Configurar los botones para las actividades correspondientes
        configurarBotones()

        // Cargar los estados de los dispositivos al iniciar la actividad
        cargarEstadosDispositivos()
    }

    private fun configurarBotones() {
        // Botón para apagar todos los dispositivos
        btnOff.setOnClickListener {
            apagarTodo()
        }

        // Botón para ir a la configuración del termostato
        termostatoButton.setOnClickListener {
            val intent = Intent(this, TermostatoSettings::class.java)
            startActivity(intent)
        }

        // Botón para ir a la configuración de la TV
        findViewById<Button>(R.id.btn_tv).setOnClickListener {
            val intent = Intent(this, TvControlActivity::class.java)
            startActivity(intent)
        }

        // Botón para ir a la configuración de las luces
        findViewById<Button>(R.id.btn_lights).setOnClickListener {
            val intent = Intent(this, LucesSettings::class.java)
            startActivity(intent)
        }

        // Botón para ir a la configuración de las persianas
        findViewById<Button>(R.id.btn_blinds).setOnClickListener {
            val intent = Intent(this, PersianasControlActivity::class.java)
            startActivity(intent)
        }
    }

    private fun apagarTodo() {
        // Actualizar el estado de la TV
        tvStatus.text = "Apagado"
        volumeStatus.text = "Volumen: 0"

        // Actualizar el estado de las persianas
        blindsPercentage.text = "Apertura: 0%"

        // Actualizar el estado de las luces
        lightStatus.text = "Luces Apagadas"

        // Actualizar el estado del termostato
        termostatoStatus.text = "Temperatura: 0ºC"

        // Guardar el estado de apagado en SharedPreferences
        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("tvState", false) // Estado de la TV
        editor.putInt("volumeLevel", 0) // Volumen de la TV
        editor.apply() // Aplicar cambios

        val blindsSharedPreferences = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE)
        val blindsEditor = blindsSharedPreferences.edit()
        blindsEditor.putInt("blindsPercentage", 0) // Porcentaje de las persianas
        blindsEditor.apply() // Aplicar cambios

        val lucesSharedPreferences = getSharedPreferences("LucesSettings", Context.MODE_PRIVATE)
        val lucesEditor = lucesSharedPreferences.edit()
        lucesEditor.putBoolean("lucesState", false) // Estado de las luces
        lucesEditor.apply() // Aplicar cambios

        val thermostatSharedPreferences = getSharedPreferences("ThermostatSettings", Context.MODE_PRIVATE)
        val thermostatEditor = thermostatSharedPreferences.edit()
        thermostatEditor.putInt("thermostatTemperature", 0) // Temperatura del termostato
        thermostatEditor.apply() // Aplicar cambios

        // Mostrar un mensaje de confirmación
        Toast.makeText(this, "Todos los dispositivos han sido apagados", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Cargar los estados de los dispositivos al volver a la actividad
        cargarEstadosDispositivos()
    }

    private fun cargarEstadosDispositivos() {
        // Recuperar y actualizar el estado de la TV
        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        tvStatus.text = if (isTvOn) "Encendido" else "Apagado"
        if (isTvOn) {
            volumeStatus.text = "Volumen: $volumeLevel"
            volumeStatus.visibility = TextView.VISIBLE // Hacer visible el volumen si la TV está encendida
        } else {
            volumeStatus.visibility = TextView.INVISIBLE // Hacer invisible el volumen si la TV está apagada
        }

        // Recuperar y actualizar el porcentaje de persianas
        val blindsPercentageValue = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE)
            .getInt("blindsPercentage", 0)
        blindsPercentage.text = "Apertura: ${blindsPercentageValue}%"

        // Recuperar el estado de las luces
        val isLucesOn = getSharedPreferences("LucesSettings", Context.MODE_PRIVATE)
            .getBoolean("lucesState", false)
        lightStatus.text = if (isLucesOn) "Luces Encendidas" else "Luces Apagadas"

        // Recuperar la temperatura del termostato
        val thermostatTemperature = getSharedPreferences("ThermostatSettings", Context.MODE_PRIVATE)
            .getInt("thermostatTemperature", 50)
        termostatoStatus.text = "Temperatura: ${thermostatTemperature}ºC"
    }
}
