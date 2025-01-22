package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class PersianasControlActivity: AppCompatActivity() {
    private lateinit var percentageText: TextView
    private lateinit var seekBarBlinds: SeekBar
    private lateinit var botonActualizar:Button
    private lateinit var progressBarActualizar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persianas)

        percentageText = findViewById(R.id.percentage_text)
        seekBarBlinds = findViewById(R.id.seekbar_blinds)
        botonActualizar = findViewById<Button>(R.id.buttonActualizar)
        progressBarActualizar = findViewById(R.id.progressBarActualizar)

        // Configurar el botón de "Actualizar Firmware"
        botonActualizar.setOnClickListener {
            // Mostrar el Toast y el ProgressBar
            Toast.makeText(this, "Actualizando Firmware de las persianas...", Toast.LENGTH_SHORT).show()
            progressBarActualizar.visibility = ProgressBar.VISIBLE

            // Generar un retardo aleatorio entre 2 y 5 segundos (2000 ms y 5000 ms)
            val delayMillis = Random.nextInt(1000, 15001)

            // Ocultar el ProgressBar después del tiempo aleatorio
            Handler(Looper.getMainLooper()).postDelayed({
                progressBarActualizar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this, "Firmware actualizado correctamente", Toast.LENGTH_SHORT).show()
            }, delayMillis.toLong())
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
