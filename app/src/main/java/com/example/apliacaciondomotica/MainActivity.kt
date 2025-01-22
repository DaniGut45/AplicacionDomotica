package com.example.apliacaciondomotica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var blindsPercentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tv_status)
        val volumeStatus = findViewById<TextView>(R.id.volume_status)
        blindsPercentage = findViewById<TextView>(R.id.blinds_percentage)

        // Recuperar valores desde SharedPreferences
        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        // Actualizar los TextView de TV
        tvStatus.text = if (isTvOn) "Encendido" else "Apagado"
        volumeStatus.text = "Volumen: $volumeLevel"

        // Recuperar el porcentaje de apertura de persianas
        val blindsPercentageValue = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE)
            .getInt("blindsPercentage", 0)

        // Actualizar el TextView de persianas
        blindsPercentage.text = "Apertura: ${blindsPercentageValue}%"

        // Botón "TV"
        val btnTv = findViewById<Button>(R.id.btn_tv)
        btnTv.setOnClickListener {
            val intent = Intent(this, TvControlActivity::class.java)
            startActivity(intent)
        }

        val btnConnect = findViewById<Button>(R.id.btn_connect_device)
        btnConnect.setOnClickListener{
            Toast.makeText(this, "Conectando Dispositivo...", Toast.LENGTH_SHORT).show()
        }


        // Botón "Persianas"
        val btnBlinds = findViewById<Button>(R.id.btn_blinds)
        btnBlinds.setOnClickListener {
            val intent = Intent(this, PersianasControlActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Actualizar los valores al volver a esta actividad
        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        val tvStatus = findViewById<TextView>(R.id.tv_status)
        val volumeStatus = findViewById<TextView>(R.id.volume_status)

        tvStatus.text = if (isTvOn) "Encendido" else "Apagado"
        volumeStatus.text = "Volumen: $volumeLevel"

        // Recuperar y actualizar el porcentaje de persianas
        val blindsPercentageValue = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE)
            .getInt("blindsPercentage", 0)

        blindsPercentage.text = "Apertura: ${blindsPercentageValue}%"
    }
}
