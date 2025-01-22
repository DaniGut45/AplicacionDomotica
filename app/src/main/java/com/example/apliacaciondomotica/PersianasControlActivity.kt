package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PersianasControlActivity: AppCompatActivity() {
    private lateinit var percentageText: TextView
    private lateinit var seekBarBlinds: SeekBar
    private lateinit var botonActualizar:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persianas)

        percentageText = findViewById(R.id.percentage_text)
        seekBarBlinds = findViewById(R.id.seekbar_blinds)
        botonActualizar = findViewById<Button>(R.id.buttonActualizar)

        botonActualizar.setOnClickListener {
            Toast.makeText(this, "Actualizando Firmware...", Toast.LENGTH_SHORT).show()
        }

        // Recuperar el porcentaje guardado en SharedPreferences
        val sharedPreferences = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE)
        val savedPercentage = sharedPreferences.getInt("blindsPercentage", 0)
        seekBarBlinds.progress = savedPercentage / 10
        percentageText.text = "Apertura: ${savedPercentage}%"

        // Actualizar el porcentaje al mover el SeekBar
        seekBarBlinds.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = progress * 10
                percentageText.text = "Apertura: ${percentage}%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        // Guardar el porcentaje al salir de la actividad
        val percentage = seekBarBlinds.progress * 10
        val editor = getSharedPreferences("BlindsSettings", Context.MODE_PRIVATE).edit()
        editor.putInt("blindsPercentage", percentage)
        editor.apply()
    }
}
