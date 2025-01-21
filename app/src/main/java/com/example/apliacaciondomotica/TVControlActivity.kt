package com.example.apliacaciondomotica

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TvControlActivity : AppCompatActivity() {
    private lateinit var tvSwitch: Switch
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var volumeText: TextView
    private lateinit var botonActualizar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_tv)

        tvSwitch = findViewById(R.id.switch_tv)
        volumeSeekBar = findViewById(R.id.volume_seekbar)
        volumeText = findViewById(R.id.volume_text)
        botonActualizar = findViewById<Button>(R.id.buttonActualizar)

        val sharedPreferences = getSharedPreferences("TvSettings", Context.MODE_PRIVATE)

        // Recuperar estado de la TV y volumen
        val isTvOn = sharedPreferences.getBoolean("tvState", false)
        val volumeLevel = sharedPreferences.getInt("volumeLevel", 50)

        // Establecer los valores en los componentes UI
        tvSwitch.isChecked = isTvOn
        volumeSeekBar.progress = volumeLevel
        volumeText.text = "Volumen: $volumeLevel"

        botonActualizar.setOnClickListener {
            Toast.makeText(this, "Actualizando Firmware...", Toast.LENGTH_SHORT).show()
        }

        // Actualizar el estado del Switch
        tvSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("tvState", isChecked)
            editor.apply()
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

    private fun updateMainActivityTextView(isTvOn: Boolean, volumeLevel: Int) {
        // Aquí, enviamos los datos de estado de la TV y volumen a MainActivity
        val intent = Intent()
        intent.putExtra("tvState", isTvOn)
        intent.putExtra("volumeLevel", volumeLevel)
        setResult(RESULT_OK, intent)
        // No llamamos a finish() aquí
    }
}
