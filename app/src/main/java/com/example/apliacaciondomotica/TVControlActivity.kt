package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class TvControlActivity : AppCompatActivity() {
    private lateinit var tvSwitch: Switch
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var volumeText: TextView
    private lateinit var botonActualizar: Button
    private lateinit var progressBarActualizar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_tv)

        tvSwitch = findViewById(R.id.switch_tv)
        volumeSeekBar = findViewById(R.id.volume_seekbar)
        volumeText = findViewById(R.id.volume_text)
        botonActualizar = findViewById(R.id.buttonActualizar)
        progressBarActualizar = findViewById(R.id.progressBarActualizar)

        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)


        // Recuperar estado de la TV y volumen
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        // Establecer los valores en los componentes UI
        tvSwitch.isChecked = isTvOn
        volumeSeekBar.progress = volumeLevel
        volumeText.text = "Volumen: $volumeLevel"

        // Manejar la visibilidad del SeekBar y TextView de volumen según el estado de la TV
        updateVolumeVisibility(isTvOn)

        // Configurar el botón de "Actualizar Firmware"
        botonActualizar.setOnClickListener {
            // Mostrar el Toast y el ProgressBar
            Toast.makeText(this, "Actualizando Firmware de la TV...", Toast.LENGTH_SHORT).show()
            progressBarActualizar.visibility = ProgressBar.VISIBLE

            // Generar un retardo aleatorio entre 2 y 5 segundos (2000 ms y 5000 ms)
            val delayMillis = Random.nextInt(1000, 15001)

            // Ocultar el ProgressBar después del tiempo aleatorio
            Handler(Looper.getMainLooper()).postDelayed({
                progressBarActualizar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this, "Firmware actualizado correctamente", Toast.LENGTH_SHORT).show()
            }, delayMillis.toLong())
        }

        // Actualizar el estado del Switch
        tvSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("tvState", isChecked)
            editor.apply()

            // Actualizar la visibilidad del SeekBar y el TextView de volumen
            updateVolumeVisibility(isChecked)

            // Actualizar el TextView en MainActivity al cambiar el Switch
            updateMainActivityTextView(isChecked, volumeLevel)
        }

        // Actualizar el volumen y mostrarlo en el TextView
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeText.text = "Volumen: $progress"
                val editor = sharedPreferences.edit()
                editor.putInt("volumeLevel", progress)
                editor.apply()
                // Actualizar el TextView en MainActivity al cambiar el volumen
                updateMainActivityTextView(tvSwitch.isChecked, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateVolumeVisibility(isTvOn: Boolean) {
        if (isTvOn) {
            volumeText.visibility = TextView.VISIBLE
            volumeSeekBar.visibility = SeekBar.VISIBLE
        } else {
            volumeText.visibility = TextView.INVISIBLE
            volumeSeekBar.visibility = SeekBar.INVISIBLE
        }
    }

    private fun updateMainActivityTextView(isTvOn: Boolean, volumeLevel: Int) {
        // Aquí, enviamos los datos de estado de la TV y volumen a MainActivity
        val intent = Intent()
        intent.putExtra("tvState", isTvOn)
        intent.putExtra("volumeLevel", volumeLevel)
        setResult(RESULT_OK, intent)
        // No llamamos a finish() aquí
    }
}
